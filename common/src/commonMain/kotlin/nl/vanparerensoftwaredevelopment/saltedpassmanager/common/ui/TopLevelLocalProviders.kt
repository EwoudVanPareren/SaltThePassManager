package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui

import androidx.compose.runtime.*
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.LocalAppConfiguration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.store.DataStore
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