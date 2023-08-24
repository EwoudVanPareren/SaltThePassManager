package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

internal lateinit var UserFilesInstance: UserFiles

actual fun UserFiles(qualifier: String, organization: String, appId: String): UserFiles = UserFilesInstance