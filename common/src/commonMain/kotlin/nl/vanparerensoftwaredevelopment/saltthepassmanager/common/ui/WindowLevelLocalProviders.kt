package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.LocalAppConfiguration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.windowsize.LocalWindowSize
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.windowsize.WindowSize

/**
 * Convenience method for applying window-level [CompositionLocal]s.
 */
@Composable
fun WindowLevelLocalProviders(
    windowSize: DpSize,
    content: @Composable () -> Unit
) {
    val configDensity = LocalAppConfiguration.current.density
    val currentDensity = LocalDensity.current

    val newDensity = Density(
        density = currentDensity.density * configDensity.densityMultiplier,
        fontScale = currentDensity.fontScale * (configDensity.fontMultiplier / configDensity.densityMultiplier)
    )

    CompositionLocalProvider(
        LocalDensity provides newDensity,
        LocalWindowSize provides WindowSize.from(windowSize / configDensity.densityMultiplier)
    ) {
        content()
    }
}