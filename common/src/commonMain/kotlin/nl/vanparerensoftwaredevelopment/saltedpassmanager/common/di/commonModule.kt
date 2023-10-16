package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.di

import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.configuration.ConfigurationScreenModel
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.main.MainScreenModel
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.main.MainScreenModelForm
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.StoredAccountsService
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.StoredAccountsServices
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model.UserFiles
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.file.FileDataStore
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.store.DataStore
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

/**
 * A common Kodein module with the bindings used by all platforms.
 */
val commonModule = DI.Module(name = DIConstants.Modules.COMMON) {
    bindSingleton<DataStore<Configuration>> {
        val dirs: UserFiles = instance()
        val file = dirs.preferences.resolve("config.json")
        FileDataStore(file, Configuration())
    }
    bindSingleton<StoredAccountsService> {
        val dirs: UserFiles = instance()
        StoredAccountsServices.file(
            dirs.dataDir.resolve("data.json")
        )
    }
    bindSingleton { MainScreenModelForm() }

    bindProvider<MainScreenModel> {
        MainScreenModel(instance(), instance())
    }
    bindProvider<ConfigurationScreenModel> {
        ConfigurationScreenModel(instance())
    }
}
