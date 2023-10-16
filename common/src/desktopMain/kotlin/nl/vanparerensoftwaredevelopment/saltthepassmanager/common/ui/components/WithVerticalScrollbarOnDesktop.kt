package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A component that adds a scrollbar on desktop.
 * Outside of desktop, it has no effect and simply adds the content
 * with zero padding values.
 */
@Composable
actual fun WithVerticalScrollbarOnDesktop(
    scrollState: ScrollState,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val adapter = rememberScrollbarAdapter(scrollState)
    Box {
        content(PaddingValues(end = 10.dp))
        VerticalScrollbar(
            adapter,
            modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight()
        )
    }
}