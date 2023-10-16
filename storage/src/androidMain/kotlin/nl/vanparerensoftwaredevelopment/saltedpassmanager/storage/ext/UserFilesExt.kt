package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.ext

import android.content.Context
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.UserFiles

/**
 * Create a [UserFiles] from the given [Context], pointing
 * to the app's private storage.
 */
fun UserFiles.Companion.from(context: Context): UserFiles {
    val dataDir = context.dataDir
    return UserFiles(
        dataDir = dataDir,
        localDataDir = dataDir,
        preferences = dataDir.resolve("config")
    )
}