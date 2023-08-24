package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Window size, described as width and height classes.
 *
 * This is useful for making layout choices inside composables,
 * though it should only be used for making choices on a window-level.
 *
 * Take care not to use this to control smaller components.
 */
data class WindowSize(
    val width: WindowWidthClass,
    val height: WindowHeightClass
) {
    companion object {
        fun from(width: Dp, height: Dp) = WindowSize(
            width = WindowWidthClass.values().last {
                it.minWidth == 0.dp || width >= it.minWidth
            },
            height = WindowHeightClass.values().last {
                it.minHeight == 0.dp || height >= it.minHeight
            }
        )

        val current: WindowSize
            @Composable
            get() = LocalWindowSize.current
    }
}
