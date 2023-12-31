package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.configuration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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

    private val _keepMasterPasswordOnClearForm = Field(
        getter = { keepMasterPasswordOnClearForm },
        updater = { copy(keepMasterPasswordOnClearForm = it) }
    )
    val keepMasterPasswordOnClearForm by _keepMasterPasswordOnClearForm
    fun changeKeepMasterPasswordOnClearForm(value: Boolean) =
        _keepMasterPasswordOnClearForm.change(value)

    private val _theme = Field(
        getter = { theme },
        updater = { copy(theme = it) }
    )
    val theme by _theme
    fun changeTheme(theme: Configuration.Theme) =_theme.change(theme)

    private val _density = Field(
        getter = { density },
        updater = { copy(density = it) }
    )
    val density by _density
    fun changeDensity(density: Configuration.DensityLevel) = _density.change(density)

    private val _enableTrayIcon = Field(
        getter = { enableTrayIcon },
        updater = { copy(enableTrayIcon = it) }
    )
    val enableTrayIcon by _enableTrayIcon
    fun changeEnableTrayIcon(enableTrayIcon: Boolean) = _enableTrayIcon.change(enableTrayIcon)

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
        }.launchIn(screenModelScope)
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
            screenModelScope.launch {
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