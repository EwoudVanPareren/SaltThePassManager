package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.file

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.readJson
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.writeJson
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStore
import java.io.File
import java.io.FileNotFoundException

/**
 * A simple file store that reads a JSON file from disk
 * and overwrites it on every update.
 *
 * This store caches the value after the first read, assuming that
 * the file will not be modified in the meantime from anywhere else.
 *
 * Only one [FileDataStore] should be used at a time for each file.
 * Using multiple [FileDataStore]s for the same file will cause
 * inconsistent behavior.
 */
class FileDataStore<T: Any>(
    private val file: File,
    private val defaultValue: T,
    private val serializer: KSerializer<T>,
    private val json: Json = Json.Default
) : DataStore<T> {
    private val lock = Mutex()
    private val cache = MutableStateFlow<T?>(null)

    override val updates = cache.filterNotNull()
        .onStart { ensureRead() }

    override suspend fun update(block: suspend (T) -> T) {
        ensureRead()
        /*
         * Using the lock and update functions is a bit overkill since
         * both address the same problem, but it shouldn't hurt to use
         * both methods anyway.
         */
        lock.withLock {
            val newValue = cache.updateAndGet { block(it!!) }!!
            file.writeJson(newValue, serializer, json)
        }
    }

    override suspend fun set(value: T) {
        lock.withLock {
            cache.value = value
            file.writeJson(value, serializer, json)
        }
    }

    override suspend fun get(): T {
        ensureRead()
        return cache.value!!
    }

    override suspend fun init() {
        ensureRead()
    }

    private suspend fun ensureRead() {
        if (cache.value == null) {
            lock.withLock {
                if (cache.value == null) {
                    try {
                        cache.value = file.readJson(serializer, json)
                    } catch (e: FileNotFoundException) {
                        cache.value = defaultValue
                    }
                }
            }
        }
    }

    companion object {
        inline operator fun <reified T: Any> invoke(
            file: File,
            defaultValue: T,
            json: Json = Json.Default
        ) = FileDataStore(file, defaultValue, serializer(), json)
    }
}