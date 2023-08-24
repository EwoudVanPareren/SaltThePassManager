package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.configuration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration

class ConfigurationScreenModel(
    private val appConfig: KStore<Configuration>
): ScreenModel {
    private var _theme by mutableStateOf(Configuration.Theme.SYSTEM)
    private var _density by mutableStateOf(Configuration.DensityLevel.NORMAL)

    val theme: Configuration.Theme get() = _theme
    val density: Configuration.DensityLevel get() = _density

    init {
        appConfig.updates.onEach { config ->
            _theme = config!!.theme
            _density = config.density
        }.launchIn(coroutineScope)
    }

    fun changeTheme(theme: Configuration.Theme) {
        coroutineScope.launch {
            appConfig.update {
                it!!.copy(theme = theme)
            }
        }
    }
    fun changeDensity(densityLevel: Configuration.DensityLevel) {
        coroutineScope.launch {
            appConfig.update {
                it!!.copy(density = densityLevel)
            }
        }
    }
}