package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/**
 * A decorative component that mimics the look of [OutlinedTextField].
 * This serves as a generic container for other form components,
 * allowing for a more consistent visual style.
 */
@Composable
fun OutlinedFormItemContainer(
    label: (@Composable () -> Unit)? = null,
    overrideFocusVisualState: Boolean? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contents: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    var containsFocus by remember { mutableStateOf(false) }
    val showFocused = overrideFocusVisualState ?: containsFocus

    CompositionLocalProvider(LocalContentColor.provides(colorScheme.onSurfaceVariant)) {
        Box(
            modifier.padding(vertical = 5.dp)
                .onFocusChanged {
                    containsFocus = it.isFocused || it.hasFocus
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .border(
                        width = if (showFocused) {
                            OutlinedTextFieldDefaults.FocusedBorderThickness
                        } else {
                            OutlinedTextFieldDefaults.UnfocusedBorderThickness
                        },
                        color = when {
                            showFocused -> colorScheme.primary
                            else -> colorScheme.outline
                        },
                        shape = OutlinedTextFieldDefaults.shape
                    )
                    .let {
                        if (onClick != null) {
                            it.clickable(onClick = onClick)
                        } else {
                            it
                        }
                    }
                    .padding(vertical = 5.dp)
            ) {
                contents()
            }
            if (label != null) {
                val labelColor = when {
                    showFocused -> colorScheme.primary
                    else -> colorScheme.onSurfaceVariant
                }
                Box(
                    modifier = Modifier
                        .offset(x = 12.dp, y = (-4).dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            RectangleShape
                        )
                        .padding(horizontal = 4.dp)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor.provides(labelColor),
                        LocalTextStyle.provides(MaterialTheme.typography.bodySmall)
                    ) {
                        label()
                    }
                }
            }
        }
    }
}