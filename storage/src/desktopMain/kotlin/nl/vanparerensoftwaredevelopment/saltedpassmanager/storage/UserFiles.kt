package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage

import dev.dirs.ProjectDirectories
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.UserFiles
import java.io.File

fun UserFiles.Companion.from(qualifier: String, organization: String, appId: String): UserFiles {
    val dirs = ProjectDirectories.from(qualifier, organization, appId)
    return UserFiles(
        dataDir = File(dirs.dataDir),
        localDataDir = File(dirs.dataLocalDir),
        preferences = File(dirs.preferenceDir)
    )
}