@file:OptIn(ExperimentalMaterial3Api::class)

package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components

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

@Composable
fun OutlinedFormItemContainer(
    label: (@Composable () -> Unit)? = null,
    overrideFocusVisualState: Boolean? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contents: @Composable () -> Unit
) {
    var containsFocus by remember { mutableStateOf(false) }
    val showFocused = overrideFocusVisualState ?: containsFocus
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
                        TextFieldDefaults.FocusedBorderThickness
                    } else {
                        TextFieldDefaults.UnfocusedBorderThickness
                    },
                    color = if (showFocused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = TextFieldDefaults.outlinedShape
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
            ProvideTextStyle(
                MaterialTheme.typography.bodySmall.let {
                    if (showFocused) {
                        it.copy(color = MaterialTheme.colorScheme.primary)
                    } else it
                }
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = 12.dp, y = (-4).dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            RectangleShape
                        )
                        .padding(horizontal = 4.dp)
                ) {
                    label()
                }
            }
        }
    }

}