package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The height class of the window.
 * This can be used to make window or screen level layout choices.
 */
enum class WindowHeightClass(val minHeight: Dp) {
    SMALL(0.dp), MEDIUM(200.dp), BIG(500.dp);

    companion object {
        val current: WindowHeightClass
            @Composable
            get() = WindowSize.current.height
    }
}