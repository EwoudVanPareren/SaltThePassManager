package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.store

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration

/**
 * An in-memory version of [DataStore] that keeps everything in-memory.
 * This is primarily for use in tests.
 *
 * It mirrors a number of internal details of [FileStore], so it can
 * be expected to behave similarly.
 *
 * @param [initialValue] the value that the in-memory store starts out with
 * @param [readTime] can be used to simulate a delay in the initial reading of
 *        data from a source.
 * @param [writeTime] can be used to simulate a delay in the writing of the data
 *        to a source.
 */
class InMemoryDataStore<T: Any>(
    private val initialValue: T,
    private val readTime: Duration = Duration.ZERO,
    private val writeTime: Duration = Duration.ZERO
): DataStore<T> {
    private val lock = Mutex()
    private val state = MutableStateFlow<T?>(null)

    override val updates: Flow<T> = state.filterNotNull().onStart {
        ensureRead()
    }

    override suspend fun get(): T {
        ensureRead()
        return state.value!!
    }

    override suspend fun set(value: T) {
        ensureRead()
        lock.withLock {
            state.value = value
            delay(writeTime)
        }
    }

    override suspend fun update(block: suspend (T) -> T) {
        ensureRead()

        lock.withLock {
            state.update { block(it!!) }
            delay(writeTime)
        }
    }

    private suspend fun ensureRead() {
        if (state.value == null) {
            lock.withLock {
                if (state.value == null) {
                    delay(readTime)
                    state.value = initialValue
                }
            }
        }
    }
}