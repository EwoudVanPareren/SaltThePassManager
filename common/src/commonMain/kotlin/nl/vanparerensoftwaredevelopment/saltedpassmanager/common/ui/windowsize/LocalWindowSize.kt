package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.windowsize

import androidx.compose.runtime.compositionLocalOf

/**
 * In a fashion similar to many CSS frameworks, provide access to
 * window size classes from anywhere in the composition.
 *
 * This is useful for making layout choices on a window or screen level,
 * though care should be taken not to use this to control smaller
 * components.
 */
val LocalWindowSize = compositionLocalOf {
    WindowSize(WindowWidthClass.MEDIUM, WindowHeightClass.MEDIUM)
}