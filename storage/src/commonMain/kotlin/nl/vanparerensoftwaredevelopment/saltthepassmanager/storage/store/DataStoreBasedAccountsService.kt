package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store

import kotlinx.coroutines.flow.*
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.AccountAlreadyExistsException
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.AccountNotFoundException
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.DomainSuggestion
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService.*
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccount
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.associateByMulti
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.lowercaseUS
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.toCountMap
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccountsFile

/**
 * A [StoredAccountsService] backed by a [DataStore] that contains a [StoredAccountsFile].
 *
 * A single user is extremely unlikely to have more stored accounts than can
 * comfortably fit into memory, so this is a good option.
 */
internal class DataStoreBasedAccountsService(
    private val store: DataStore<StoredAccountsFile>
): StoredAccountsService {
    override val allAccounts: Flow<List<StoredAccount>> = store.updates
        .map { accountsFile ->
            accountsFile.items.sortedWith(
                Comparator.comparing<StoredAccount, String> { it.key.domainName }
                    .thenComparing<String> { it.key.domainPhrase }
            )
        }

    private val accountsPerDomain = store.updates.map { accountsFile ->
        accountsFile.items.associateByMulti {
            it.key.domainName
        }.mapValues { (_, rawAccounts) ->
            val accounts = rawAccounts.sortedBy { it.key.domainPhrase }
            /*
             * For a "default" account to use with suggestions, pick the no-domain-phrase account.
             * Alternatively, if there is only one account stored for a domain name, use that
             * by default.
             * If neither is available, forget it.
             */
            val defaultConfig = accounts.firstOrNull {
                it.domainPhrase.isEmpty()
            } ?: (accounts.first().takeIf { accounts.size == 1 })


            // We want to suggest the best variant (case-wise) of the domain name
            val suggestDomainName = defaultConfig?.domainName ?: run {
                /*
                 * Pick the variant of the domain name that occurs most often among the
                 * stored accounts.
                 *
                 * This is mostly to make certain corner-case like behavior (different accounts with
                 * different capitalization for the same domain) more predictable.
                 *
                 * Ideally, the user should use the same capitalization for the same domain name
                 * on all accounts, but let's try to make things consistent for situations where this
                 * isn't the case.
                 */
                accounts
                    .map { it.domainName }
                    .toCountMap()
                    .maxBy { it.value }
                    .key
            }

            ByDomainEntry(
                accounts = accounts,
                suggestion = DomainSuggestion(
                    defaultAccount = defaultConfig,
                    domainName = suggestDomainName
                )
            )
        }
    }

    override suspend fun suggestedDomainsFor(domainInput: String): Flow<List<DomainSuggestion>> {
        return accountsPerDomain.map { accountsPerDomain ->
            accountsPerDomain.values.map {
                it.suggestion
            }.filter {
                it.domainName.startsWith(domainInput, ignoreCase = true)
            }.sortedBy {
                it.domainNameLowercase
            }
        }
    }

    override suspend fun getAccountsFor(domainName: String): Flow<List<StoredAccount>> {
        return accountsPerDomain.map { accountsPerDomain ->
            accountsPerDomain[domainName.lowercaseUS()]?.accounts ?: emptyList()
        }
    }

    override suspend fun add(item: StoredAccount, onSameKey: AddOnSameKey) {
        store.update { old ->
            val oldIndex = old.items.indexOfFirst { it.key == item.key }
            val list = when {
                oldIndex == -1 -> old.items + item

                else -> when (onSameKey) {
                    AddOnSameKey.ERROR ->
                        throw AccountAlreadyExistsException("Account with ID ${old.items[oldIndex].key} already exists")

                    AddOnSameKey.OVERWRITE -> {
                        val list = old.items.toMutableList()
                        list[oldIndex] = item
                        list.toList()
                    }
                }
            }
            old.copy(items = list)
        }
    }

    override suspend fun remove(keys: Collection<StoredAccount.Key>, ignoreMissing: Boolean) {
        if (keys.isEmpty()) return
        store.update { old ->
            val keySet = keys.toMutableSet()
            val list = old.items.filter {
                !keySet.remove(it.key)
            }
            if (!ignoreMissing && keySet.isNotEmpty()) {
                throw AccountNotFoundException("Failed to find and delete accounts: $keySet")
            }
            old.copy(items = list)
        }
    }

    private data class ByDomainEntry(
        val accounts: List<StoredAccount>,
        val suggestion: DomainSuggestion
    )
}