package nl.vanparerensoftwaredevelopment.saltedpassmanager.android

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator

/**
 * Get the window size for the given activity.
 */
@Composable
fun getWindowSize(activity: Activity): DpSize {
    LocalConfiguration.current
    val density = LocalDensity.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    return with(density) { metrics.bounds.toComposeRect().size.toDpSize() }
}