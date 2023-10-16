package utils

import it.sauronsoftware.junique.AlreadyLockedException
import it.sauronsoftware.junique.JUnique
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

private const val MESSAGE_OPEN = "open"

/**
 * Ensure that only one instance of the application can run at a time for
 * this user on this machine.
 *
 * @param [id] the unique ID of the application
 * @param [block] the code to execute if this is the first/only instance
 *        at the moment. Typically, this would contain the application's
 *        main code. Once this block finishes, the one-instance lock is
 *        released.
 */
fun withJUniqueLock(id: String, block: (openAttempt: Flow<Unit>) -> Unit) {
    try {
        val messages = MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        JUnique.acquireLock(id) { message ->
            if (message == MESSAGE_OPEN) {
                messages.tryEmit(Unit)
            }
            null
        }
        try {
            block(messages)
        } finally {
            JUnique.releaseLock(id)
        }
    } catch (e: AlreadyLockedException) {
        JUnique.sendMessage(id, MESSAGE_OPEN)
    }
}