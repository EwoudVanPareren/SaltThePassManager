package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.di

import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.configuration.ConfigurationScreenModel
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.main.MainScreenModel
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.main.MainScreenModelForm
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsServices
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.UserFiles
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.file.FileDataStore
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStore
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
