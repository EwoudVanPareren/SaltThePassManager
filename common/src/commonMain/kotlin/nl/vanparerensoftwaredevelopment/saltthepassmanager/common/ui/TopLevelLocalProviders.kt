package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui

import androidx.compose.runtime.*
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.LocalAppConfiguration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStore
import org.kodein.di.DI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

/**
 * Convenience method for applying top-level dependency injection
 * and [CompositionLocal]s.
 */
@Composable
fun TopLevelLocalProviders(
    di: DI,
    content: @Composable () -> Unit
) {
    withDI(di) {
        val config by remember {
            val store: DataStore<Configuration> by di.instance()
            store.updates
        }.collectAsState(Configuration.DEFAULT)

        CompositionLocalProvider(
            LocalAppConfiguration provides config
        ) {
            content()
        }
    }
}