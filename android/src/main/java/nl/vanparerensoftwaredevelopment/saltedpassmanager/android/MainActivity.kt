package nl.vanparerensoftwaredevelopment.saltedpassmanager.android

import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.SaltThePassManagerRoot
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.loading.LoadingScreen
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.theme.SaltThePassManagerTheme
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.TopLevelLocalProviders
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.WindowLevelLocalProviders
import nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.store.DataStore
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance

/**
 * The main activity of SaltThePass Manager.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val di = (application as SaltThePassManagerApplication).di
        setContent {
            TopLevelLocalProviders(di) {
                if (isConfigLoaded()) {
                    WindowLevelLocalProviders(getWindowSize(this)) {
                        SaltThePassManagerRoot()
                    }
                } else {
                    SaltThePassManagerTheme(overrideTheme = Configuration.Theme.SYSTEM) {
                        LoadingScreen(overrideDarkMode = isSystemInDarkTheme())
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Utility method for checking whether or not the configuration
         * data store has completed loading.
         *
         * This will always return false on the first call, regardless of
         * whether the configuration is already loaded or not. It is
         * recommended to use this only in a top-level spot.
         */
        @Composable
        private fun isConfigLoaded(): Boolean {
            var configLoaded by remember {
                mutableStateOf(false)
            }
            val configStore by rememberDI { instance<DataStore<Configuration>>() }
            LaunchedEffect(null) {
                configStore.init()
                configLoaded = true
            }
            return configLoaded
        }
    }
}