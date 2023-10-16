package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage

import kotlinx.coroutines.flow.Flow
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model.DomainSuggestion
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model.StoredAccount

/**
 * The service responsible for loading and saving stored account data.
 */
interface StoredAccountsService {
    /**
     * All accounts, ordered first by domain name
     * and then domain phrase.
     */
    val allAccounts: Flow<List<StoredAccount>>

    /**
     * Get the suggested domains for a given input.
     * The returned flow will, when subscribed, emit a list of all the requested domain name
     * suggestions, and will continue to emit updates while the subscription is active.
     *
     * The list of suggestions will be ordered by the alphabetical order of their domain names.
     *
     * The input is interpreted in a case-insensitive fashion. The list of stored accounts
     * will include all domains that have
     */
    suspend fun suggestedDomainsFor(domainInput: String): Flow<List<DomainSuggestion>>

    /**
     * Get the accounts that correspond to a given domain name.
     *
     * The domain name is interpreted in a case-insensitive fashion.
     */
    suspend fun getAccountsFor(domainName: String): Flow<List<StoredAccount>>

    /**
     * Add a new account.
     *
     * If [onSameKey] is set to [AddOnSameKey.ERROR], this method will throw an
     * [AccountAlreadyExistsException] if an item with the same key already exists.
     * If it is set to [AddOnSameKey.OVERWRITE], it just quietly overwrites the
     * account data.
     */
    suspend fun add(item: StoredAccount, onSameKey: AddOnSameKey = AddOnSameKey.ERROR)

    /**
     * Remove accounts.
     *
     * If [ignoreMissing] is set to false, this method will throw an
     * [AccountNotFoundException] if at least one of the given [keys] has
     * no account associated with it.
     */
    suspend fun remove(keys: Collection<StoredAccount.Key>, ignoreMissing: Boolean = false)

    suspend fun remove(key: StoredAccount.Key, ignoreMissing: Boolean = false) =
        remove(listOf(key), ignoreMissing)

    /**
     * Whether to throw an error or overwrite an existing account entry.
     */
    enum class AddOnSameKey {
        ERROR, OVERWRITE
    }
}