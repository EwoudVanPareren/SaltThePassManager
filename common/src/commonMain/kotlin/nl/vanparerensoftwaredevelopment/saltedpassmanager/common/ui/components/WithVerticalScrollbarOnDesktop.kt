package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

/**
 * A component that adds a scrollbar on desktop.
 * Outside of desktop, it has no effect and simply adds the content
 * with zero padding values.
 */
@Composable
expect fun WithVerticalScrollbarOnDesktop(
    scrollState: ScrollState,
    content: @Composable (paddingValues: PaddingValues) -> Unit
)