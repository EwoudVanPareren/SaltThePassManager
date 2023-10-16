package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store

import kotlinx.coroutines.flow.Flow

/**
 * This interface represents some storage medium for reading
 * and writing a specific object (typically a data class).
 *
 * Typically, this would represent a file that contains a
 * serialized object. Alternatively, it could represent a
 * remotely stored file, database, database entry, or a
 * pure in-memory object.
 */
interface DataStore<T : Any> {
    /**
     * All the updates to the data.
     *
     * On first subscription, the data store will read the value from
     * its source and cache it in memory, so that subsequent reads will
     * not need to access the source again.
     */
    val updates: Flow<T>

    /**
     * Update the data.
     *
     * The function returns only after the data is successfully
     * committed to the store.
     */
    suspend fun update(block: suspend (T) -> T)

    /**
     * Overwrite the data with the given value.
     *
     * Generally, it is better to use [update], unless there is
     * a specific reason to overwrite the data completely.
     *
     * The function returns only after the data is successfully
     * committed to the store.
     */
    suspend fun set(value: T)

    /**
     * Get the value directly. If the in-memory cache is not yet
     * populated, it will read from the source.
     */
    suspend fun get(): T

    /**
     * Optional function that triggers an initial read from the
     * source if it has not already been read.
     *
     * Depending on the specific type of [DataStore], this may or
     * may not have an effect.
     */
    suspend fun init() {}
}