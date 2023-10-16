package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.AccountAlreadyExistsException
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.AccountNotFoundException
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.DomainSuggestion
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccount
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccountsFile
import kotlin.test.*

class DataStoreBasedAccountsServiceTest {
    private lateinit var service: DataStoreBasedAccountsService

    @BeforeTest
    fun setUp() {
        service = DataStoreBasedAccountsService(InMemoryDataStore(StoredAccountsFile(emptyList())))
    }

    /**
     * Test adding and removing accounts.
     */
    @Test
    fun testAddRemove() = runTest {
        service.add(createStoredAccount(domainName = "example.com", domainPhrase = "user"))
        service.add(createStoredAccount(domainName = "example.com", domainPhrase = "user2"))
        service.add(createStoredAccount(domainName = "github.com", domainPhrase = "user"))

        assertEquals(
            expected = setOf(
                createStoredAccount(domainName = "example.com", domainPhrase = "user"),
                createStoredAccount(domainName = "example.com", domainPhrase = "user2"),
                createStoredAccount(domainName = "github.com", domainPhrase = "user")
            ),
            actual = service.allAccounts.first().toSet(),
            message = "Failed to add items properly"
        )

        try {
            service.add(
                createStoredAccount(domainName = "github.com", domainPhrase = "user", versionName = "2"),
                onSameKey = StoredAccountsService.AddOnSameKey.ERROR
            )
            fail("Error not thrown when attempting to add an already-added account")
        } catch (_: AccountAlreadyExistsException) { }

        service.add(
            createStoredAccount(domainName = "github.com", domainPhrase = "user", versionName = "3"),
            onSameKey = StoredAccountsService.AddOnSameKey.OVERWRITE
        )

        val expected = createStoredAccount(domainName = "github.com", domainPhrase = "user", versionName = "3")
        assertEquals(
            expected = expected,
            actual = service.getAccountsFor("github.com").first().first { it.key == expected.key },
            message = "Failed to overwrite existing account data"
        )

        // Now test removal
        val removeKey = StoredAccount.Key("example.com", "user")
        service.remove(removeKey)

        assertEquals(
            expected = listOf(createStoredAccount(domainName = "example.com", domainPhrase = "user2")),
            actual = service.getAccountsFor("example.com").first(),
            message = "Failed to remove item"
        )

        // Test behavior for non-existing account removals
        try {
            service.remove(
                StoredAccount.Key("example.com", "user3"),
               ignoreMissing = false
            )
            fail("Error not thrown when attempting to remove a non-existing account")
        } catch (_: AccountNotFoundException) { }


        service.remove(
            StoredAccount.Key("example.com", "user4"),
            ignoreMissing = true
        )
    }

    @Test
    fun testGetAllAccounts() = runTest {
        listOf(
            createStoredAccount(domainName = "github.com", domainPhrase = "user"),
            createStoredAccount(domainName = "example.com", domainPhrase = "user2", versionName = "5"),
            createStoredAccount(domainName = "anotherrandomwebsite.com", domainPhrase = ""),
            createStoredAccount(domainName = "example.com", domainPhrase = "user")
        ).forEach {
            service.add(it)
        }

        // Ensure that the accounts are all added, and in the right order
        assertEquals(
            expected = listOf(
                createStoredAccount(domainName = "anotherrandomwebsite.com", domainPhrase = ""),
                createStoredAccount(domainName = "example.com", domainPhrase = "user"),
                createStoredAccount(domainName = "example.com", domainPhrase = "user2", versionName = "5"),
                createStoredAccount(domainName = "github.com", domainPhrase = "user")
            ),
            actual = service.allAccounts.first()
        )
    }

    @Test
    fun testSuggestedDomainsFor() = runTest {
        listOf(
            createStoredAccount(domainName = "abcdefg.com", domainPhrase = "", versionName = "2"),
            createStoredAccount(domainName = "abcdefg.com", domainPhrase = "two"),
            createStoredAccount(domainName = "abcd.com", domainPhrase = "user"),
            createStoredAccount(domainName = "abcd.com", domainPhrase = "user2", versionName = "5"),
            createStoredAccount(domainName = "example.com", domainPhrase = "user"),
            createStoredAccount(domainName = "github.com", domainPhrase = "user")
        ).forEach {
            service.add(it)
        }

        assertEquals(
            expected = listOf(
                DomainSuggestion(
                    domainName = "abcd.com",
                    defaultAccount = null
                ),
                DomainSuggestion(
                    domainName = "abcdefg.com",
                    defaultAccount = createStoredAccount(
                        domainName = "abcdefg.com",
                        domainPhrase = "",
                        versionName = "2"
                    )
                ),
                DomainSuggestion(
                    domainName = "example.com",
                    defaultAccount = createStoredAccount(domainName = "example.com", domainPhrase = "user")
                ),
                DomainSuggestion(
                    domainName = "github.com",
                    defaultAccount = createStoredAccount(domainName = "github.com", domainPhrase = "user")
                )
            ),
            actual = service.suggestedDomainsFor("").first()
        )

        assertEquals(
            expected = listOf(
                DomainSuggestion(
                    domainName = "abcd.com",
                    defaultAccount = null
                ),
                DomainSuggestion(
                    domainName = "abcdefg.com",
                    defaultAccount = createStoredAccount(
                        domainName = "abcdefg.com",
                        domainPhrase = "",
                        versionName = "2"
                    )
                )
            ),
            actual = service.suggestedDomainsFor("ab").first()
        )
    }

    @Test
    fun testGetAccountsFor() = runTest {
        listOf(
            createStoredAccount(domainName = "abcdefg.com", domainPhrase = "", versionName = "2"),
            createStoredAccount(domainName = "abcdefg.com", domainPhrase = "two"),
            createStoredAccount(domainName = "abcd.com", domainPhrase = "user"),
            createStoredAccount(domainName = "abcd.com", domainPhrase = "user2", versionName = "5"),
            createStoredAccount(domainName = "example.com", domainPhrase = "user"),
            createStoredAccount(domainName = "github.com", domainPhrase = "user")
        ).forEach {
            service.add(it)
        }

        assertEquals(
            expected = listOf(
                createStoredAccount(domainName = "abcd.com", domainPhrase = "user"),
                createStoredAccount(domainName = "abcd.com", domainPhrase = "user2", versionName = "5")
            ),
            actual = service.getAccountsFor("abcd.com").first()
        )

        assertEquals(
            expected = listOf(),
            actual = service.getAccountsFor("doesnotexist").first()
        )
    }

    private fun createStoredAccount(
        domainName: String,
        domainPhrase: String = "",
        versionName: String = "",
        appendSpecial: String = "",
        hasher: String = "SHA-3",
        length: Int = 20,
    ) = StoredAccount(
        domainName = domainName,
        domainPhrase = domainPhrase,
        versionName = versionName,
        appendSpecial = appendSpecial,
        hasher = hasher,
        length = length
    )
}