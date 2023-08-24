package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import android.content.Context
import androidx.startup.Initializer

/**
 * Automatically initializes the [UserFiles] on app load, to avoid
 * having to pass a [Context] when calling [UserFiles].
 */
@Suppress("unused")
class UserFilesInitializer: Initializer<UserFiles> {
    override fun create(context: Context): UserFiles {
        val dataDir = context.dataDir
        val instance = UserFiles(
            dataDir = dataDir,
            localDataDir = dataDir,
            preferences = dataDir.resolve("config")
        )
        UserFilesInstance = instance
        return instance
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}