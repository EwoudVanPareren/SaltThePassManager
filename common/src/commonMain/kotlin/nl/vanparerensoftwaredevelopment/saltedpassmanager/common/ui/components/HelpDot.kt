package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ext.isEntirelyIn
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR

/**
 * An icon that, when clicked, opens a popup with
 * helpful information for the user.
 */
@Composable
fun HelpDot(
    focusable: Boolean = false,
    modifier: Modifier = Modifier,
    maxBoxWidth: Dp = 400.dp,
    icon: ImageVector = AppIcons.Info,
    iconColor: Color? = null,
    helpContent: @Composable () -> Unit
) {
    var open by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        val buttonColors = iconColor?.let {
            IconButtonDefaults.iconButtonColors(
                contentColor = it
            )
        } ?: IconButtonDefaults.iconButtonColors()

        IconButton(
            onClick = { open = !open },
            colors = buttonColors,
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

/**
 * An icon that, when clicked, opens a popup with
 * helpful information for the user.
 */
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

/**
 * A position provider that tries to show the popup near the anchor
 * in a helpful-tooltip fashion.
 * This should take into account different screen sizes and positions
 * for the anchor (which is assumed to be a small icon).
 */
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