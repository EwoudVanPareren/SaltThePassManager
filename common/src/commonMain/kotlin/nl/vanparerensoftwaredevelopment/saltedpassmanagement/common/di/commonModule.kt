package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.di

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.configuration.ConfigurationScreenModel
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main.MainScreenModel
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main.MainScreenModelForm
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.IsStoredConfigService
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.StoredConfigServices
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.UserFiles
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val commonModule = DI.Module(name = "common") {
    bindSingleton<CoroutineScope>(tag = "storageScope") {
        instance<CoroutineScope>("appScope") + SupervisorJob()
    }
    bindSingleton<UserFiles> {
        UserFiles("org", "ewoudvanpareren", "SaltThePassManager")
    }
    bindSingleton<KStore<Configuration>> {
        val dirs: UserFiles = instance()
        val path = dirs.preferences.resolve("config.json").path
        storeOf(path, Configuration())
    }
    bindSingleton<IsStoredConfigService> {
        val dirs: UserFiles = instance()
        StoredConfigServices.file(
            dirs.dataDir.resolve("data.json").toPath(),
            instance(tag = "storageScope")
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
