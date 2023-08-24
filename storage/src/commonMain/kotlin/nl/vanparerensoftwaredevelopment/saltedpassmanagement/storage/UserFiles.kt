package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import java.io.File

/**
 * Important files.
 */
data class UserFiles internal constructor(
    /**
     * Where to store application data in general.
     */
    val dataDir: File,
    /**
     * Where to store application data that is specific
     * to the current machine.
     * On some platforms, this may be the same path as [dataDir].
     */
    val localDataDir: File,
    /**
     * Where to store user preferences.
     * On some platforms, this may be the same path as [dataDir].
     */
    val preferences: File
)

/**
 * Get the [UserFiles] for the current application.
 *
 * The values passed to [qualifier], [organization] and [appId] may or may not be used,
 * depending on the platform. Using different values to get different directories is
 * not recommended.
 */
expect fun UserFiles(qualifier: String, organization: String, appId: String): UserFiles