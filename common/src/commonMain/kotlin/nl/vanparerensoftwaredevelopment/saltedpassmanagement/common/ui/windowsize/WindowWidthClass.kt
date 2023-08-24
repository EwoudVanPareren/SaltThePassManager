package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthClass(val minWidth: Dp) {
    TINY(0.dp),
    SMALL(200.dp),
    MEDIUM(500.dp),
    LARGE(1200.dp);

    companion object {
        val current: WindowWidthClass
            @Composable
            get() = WindowSize.current.width
    }
}