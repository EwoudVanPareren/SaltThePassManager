package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ext.isEntirelyIn
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.resources.MR

@Composable
fun HelpDot(
    focusable: Boolean = false,
    modifier: Modifier = Modifier,
    maxBoxWidth: Dp = 400.dp,
    icon: ImageVector = AppIcons.Info,
    helpContent: @Composable () -> Unit
) {
    var open by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(
            onClick = { open = !open },
            modifier = Modifier.focusProperties {
                canFocus = focusable
            }.pointerHoverIcon(PointerIcon.Default)
        ) {
            Icon(icon, contentDescription = stringResource(MR.strings.help_dot_description))
        }
        if (open) {
            Popup(
                popupPositionProvider = HelpPopupPositionProvider(),
                onDismissRequest = { open = false }
            ) {
                Card(
                    elevation = CardDefaults.elevatedCardElevation(),
                    colors = CardDefaults.elevatedCardColors(),
                    modifier = Modifier.let {
                        if (maxBoxWidth.isSpecified) {
                            it.widthIn(max = maxBoxWidth)
                        } else it
                    }.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        open = false
                    }
                ) {
                    Box(Modifier.padding(10.dp)) {
                        helpContent()
                    }
                }
            }
        }
    }
}

@Composable
fun HelpDot(
    text: AnnotatedString,
    focusable: Boolean = false,
    modifier: Modifier = Modifier,
    maxBoxWidth: Dp = 400.dp
) {
    HelpDot(
        focusable = focusable,
        modifier = modifier,
        maxBoxWidth = maxBoxWidth
    ) {
        Text(text)
    }
}

@Composable
fun HelpDot(
    text: String,
    focusable: Boolean = false,
    modifier: Modifier = Modifier,
    maxBoxWidth: Dp = 400.dp
) {
    HelpDot(
        focusable = focusable,
        modifier = modifier,
        maxBoxWidth = maxBoxWidth
    ) {
        Text(text)
    }
}

private class HelpPopupPositionProvider: PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val xLeft = anchorBounds.left - popupContentSize.width
        val xRight = anchorBounds.right
        val yAbove = anchorBounds.top - popupContentSize.height
        val yBelow = anchorBounds.bottom
        val xCentered = anchorBounds.center.x - (popupContentSize.width / 2)
        val yCentered = anchorBounds.center.y - (popupContentSize.height / 2)

        val tryThese = when (layoutDirection) {
            LayoutDirection.Ltr -> listOf(
                IntOffset(xRight, yCentered),
                IntOffset(xRight, anchorBounds.top),
                IntOffset(xLeft, yCentered),
                IntOffset(xLeft, anchorBounds.top)
            )
            LayoutDirection.Rtl -> listOf(
                IntOffset(xLeft, yCentered),
                IntOffset(xLeft, anchorBounds.top),
                IntOffset(xRight, yCentered),
                IntOffset(xRight, anchorBounds.top)
            )
        } + listOf(
            IntOffset(xCentered, yBelow),
            IntOffset(xCentered, yAbove)
        )

        val found = tryThese.firstOrNull { (x, y) ->
            val xRange = x .. (x + popupContentSize.width)
            val yRange = y .. (y + popupContentSize.height)

            xRange isEntirelyIn 0..windowSize.width
                    && yRange isEntirelyIn 0..windowSize.height
        }

        return found ?: run {
            val preferred = tryThese.first()
            IntOffset(
                x = when (layoutDirection) {
                        LayoutDirection.Ltr -> preferred.x
                            .coerceAtMost(windowSize.width - popupContentSize.width)
                            .coerceAtLeast(0)
                        LayoutDirection.Rtl -> preferred.x
                            .coerceAtLeast(0)
                            .coerceAtMost(windowSize.width - popupContentSize.width)
                },
                y = preferred.y.coerceAtLeast(0)
            )
        }
    }
}