package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.di

import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.ext.from
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.UserFiles
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val androidModule = DI.Module(name = DIConstants.Modules.ANDROID) {
    bindSingleton<UserFiles> {
        UserFiles.from(instance())
    }
}