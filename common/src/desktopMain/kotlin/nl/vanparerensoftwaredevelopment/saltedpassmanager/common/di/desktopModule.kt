package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.di

import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.from
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model.UserFiles
import org.kodein.di.DI
import org.kodein.di.bindSingleton

private const val USER_FILES_QUALIFIER = "org"
private const val USER_FILES_ORGANIZATION = "ewoudvanpareren"
private const val USER_FILES_APPID = "SaltThePassManager"

val desktopModule = DI.Module(name = DIConstants.Modules.DESKTOP) {
    bindSingleton<UserFiles> {
        UserFiles.from(
            USER_FILES_QUALIFIER,
            USER_FILES_ORGANIZATION,
            USER_FILES_APPID
        )
    }
}