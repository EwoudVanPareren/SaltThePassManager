package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage

import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStoreBasedAccountsService
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccountsFile
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.file.FileDataStore
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccount
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.InMemoryDataStore
import java.io.File
import kotlin.time.Duration

/**
 * Object that provides access to different kinds of
 * [StoredAccountsService]s.
 *
 * In the future, support may be added for remote storage formats,
 * allowing stored accounts to be synchronized between devices.
 */
object StoredAccountsServices {
    /**
     * Create a [StoredAccountsService] that uses a (local) file
     * for persistence.
     */
    fun file(file: File): StoredAccountsService =
        DataStoreBasedAccountsService(
            FileDataStore(file, StoredAccountsFile(emptyList()))
        )

    /**
     * Create a [StoredAccountsService] that has no persistence
     * and only keeps the data in memory.
     * This is mostly useful for testing.
     *
     * @param [initialValue] the value to start out with
     * @param [readTime] can be used to simulate a delay in the initial reading of
     *        data from a source.
     * @param [writeTime] can be used to simulate a delay in the writing of the data
     *        to a source.
     */
    fun inMemory(
        initialValue: List<StoredAccount>,
        readTime: Duration = Duration.ZERO,
        writeTime: Duration = Duration.ZERO
    ): StoredAccountsService =
        DataStoreBasedAccountsService(
            InMemoryDataStore(
                StoredAccountsFile(initialValue),
                readTime, writeTime
            )
        )
}
