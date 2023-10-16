package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.configuration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mu.KotlinLogging
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.Platform
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStore
import kotlin.reflect.KProperty

private val logger = KotlinLogging.logger {}

/**
 * The screen model (/viewmodel) for [ConfigurationScreen].
 */
class ConfigurationScreenModel(
    private val appConfig: DataStore<Configuration>
): ScreenModel {
    private val extractors = mutableListOf<(Configuration) -> Unit>()

    private val _theme = Field(
        getter = { theme },
        updater = { copy(theme = it) }
    )
    val theme by _theme
    fun changeTheme(theme: Configuration.Theme) = _theme.change(theme)

    private val _density = Field(
        getter = { density },
        updater = { copy(density = it) }
    )
    val density by _density
    fun changeDensity(density: Configuration.DensityLevel) = _density.change(density)

    private val _closeToTray = Field(
        getter = { closeToTray },
        updater = { copy(closeToTray = it) }
    )
    val closeToTray by _closeToTray
    fun changeCloseToTray(closeToTray: Boolean) = _closeToTray.change(closeToTray)

    private val _trayIconColor = Field(
        getter = { trayIconColor },
        updater = { copy(trayIconColor = it) }
    )
    val trayIconColor by _trayIconColor
    fun changeTrayColor(newColor: Configuration.TrayIconColor) = _trayIconColor.change(newColor)

    val showTrayOptions = Platform.isTraySupported

    init {
        appConfig.updates.onEach { config ->
            extractors.onEach { it(config) }
        }.launchIn(coroutineScope)
    }

    /**
     * Convenience class to reduce some boilerplate.
     */
    private inner class Field<T>(
        getter: Configuration.() -> T,
        private val updater: Configuration.(T) -> Configuration
    ) {
        private var state by mutableStateOf(Configuration.DEFAULT.getter())

        init {
            extractors += { state = it.getter() }
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = state

        fun change(value: T) {
            coroutineScope.launch {
                try {
                    appConfig.update {
                        it.updater(value)
                    }
                } catch (e: Throwable) {
                    logger.error(e) {
                        "Failed to write config"
                    }
                }
            }
        }
    }
}