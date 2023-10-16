package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config

import androidx.compose.runtime.compositionLocalOf

/**
 * The app's configuration options, made available inside composables.
 */
val LocalAppConfiguration = compositionLocalOf {
    Configuration()
}