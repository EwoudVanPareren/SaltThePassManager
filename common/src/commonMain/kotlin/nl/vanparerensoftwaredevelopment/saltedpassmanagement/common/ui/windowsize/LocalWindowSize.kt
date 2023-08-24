package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize

/**
 * In a fashion similar to many CSS frameworks, provide access to
 * window size classes from anywhere in the composition.
 *
 * This is useful for making layout choices on a window-level, though
 * care should be taken not to use this to control smaller components.
 */
val LocalWindowSize = compositionLocalOf {
    WindowSize(WindowWidthClass.MEDIUM, WindowHeightClass.MEDIUM)
}

/**
 * Convenience method for calculating and providing window size classes
 * inside composables.
 */
@Composable
fun withLocalWindowSize(size: DpSize, content: @Composable () -> Unit) {
    val windowSize = WindowSize.from(size.width, size.height)
    println("Size= $size, widthClass = ${windowSize.width.name}, heightClass = ${windowSize.height.name}")
    CompositionLocalProvider(
        // Using "values" explicitly here, because the IDE keeps marking it as
        // an error otherwise.
        values = arrayOf(LocalWindowSize.provides(windowSize)),
        content
    )
}