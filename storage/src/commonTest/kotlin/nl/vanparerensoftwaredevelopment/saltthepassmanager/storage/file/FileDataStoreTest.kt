package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.file

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccount
import java.io.File
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class FileDataStoreTest {
    lateinit var tempFile: File

    @BeforeTest
    fun setUp() {
        // Get a temporary file path
        tempFile = File.createTempFile("stpmtest", "json")
        // Delete it, we don't want to write any file until the test
        tempFile.delete()
    }

    @AfterTest
    fun tearDown() {
        tempFile.delete()
    }

    /**
     * Test the get and set functions
     */
    @Test
    fun testGetAndSet() = runTest(timeout = 30.seconds) {
        val store = FileDataStore<StoredAccount>(
            file = tempFile,
            defaultValue = createStoredAccount()
        )

        // Fetch the default value
        assertEquals(
            expected = createStoredAccount(),
            actual = store.get(),
            message = "Default value is incorrect"
        )

        store.set(createStoredAccount(versionName = "first update"))

        // Fetch the updated value
        assertEquals(
            expected = createStoredAccount(versionName = "first update"),
            actual = store.get(),
            message = "Failed to get a newly set value"
        )
    }

    /**
     * Test the update function
     */
    @Test
    fun testUpdate() = runTest(timeout = 30.seconds) {
        val store = FileDataStore<StoredAccount>(
            file = tempFile,
            defaultValue = createStoredAccount(versionName = "2")
        )

        store.update { old ->
            old.copy(length = 10)
        }

        assertEquals(
            expected = createStoredAccount(versionName = "2", length = 10),
            actual = store.get(),
            message = "Failed to update the value"
        )
    }

    /**
     * Test the updates flow
     */
    @Test
    fun testUpdatesFlow() = runTest(timeout = 30.seconds) {
        val store = FileDataStore<StoredAccount>(
            file = tempFile,
            defaultValue = createStoredAccount(versionName = "2")
        )

        assertEquals(
            expected = createStoredAccount(versionName = "2"),
            actual = store.updates.first(),
            message = "Failed to fetch the first value"
        )

        store.update { old ->
            old.copy(length = 10)
        }

        // Wait for the updates flow to return the new value (or timeout)
        val expected = createStoredAccount(versionName = "2", length = 10)
        store.updates.first { it == expected }
    }

    /**
     * Test whether the file is actually written to.
     */
    @Test
    fun testPersistence() = runTest(timeout = 30.seconds) {
        val store = FileDataStore<StoredAccount>(
            file = tempFile,
            defaultValue = createStoredAccount(versionName = "2")
        )

        store.update { old ->
            old.copy(length = 10)
        }

        // Ensure that the value is changed
        store.updates.first { it.length == 10 }

        val secondStore = FileDataStore<StoredAccount>(
            file = tempFile,
            defaultValue = createStoredAccount(versionName = "2")
        )

        assertEquals(
            expected = createStoredAccount(versionName = "2", length = 10),
            actual = secondStore.get(),
            message = "Failed to read the updated value from the file"
        )
    }

    private fun createStoredAccount(
        domainName: String = "example.com",
        domainPhrase: String = "example@example.com",
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