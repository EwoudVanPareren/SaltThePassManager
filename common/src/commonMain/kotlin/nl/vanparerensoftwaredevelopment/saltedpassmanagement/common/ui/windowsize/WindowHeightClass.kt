package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowHeightClass(val minHeight: Dp) {
    SMALL(0.dp), MEDIUM(200.dp), BIG(500.dp);

    companion object {
        val current: WindowHeightClass
            @Composable
            get() = WindowSize.current.height
    }
}