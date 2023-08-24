package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import dev.dirs.ProjectDirectories
import java.io.File

actual fun UserFiles(qualifier: String, organization: String, appId: String): UserFiles {
    val dirs = ProjectDirectories.from(qualifier, organization, appId)
    return UserFiles(
        dataDir = File(dirs.dataDir),
        localDataDir = File(dirs.dataLocalDir),
        preferences = File(dirs.preferenceDir)
    )
}