package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model

import java.io.File

/**
 * Important files for user data access.
 */
data class UserFiles(
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
) {

    /*
     * Activate the companion object, so it can act as a receiver
     * for extension functions on different platforms.
     */
    companion object
}