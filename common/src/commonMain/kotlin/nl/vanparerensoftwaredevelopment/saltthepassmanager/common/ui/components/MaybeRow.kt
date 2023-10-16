package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Measured

/**
 * Convenience component that may or may not act as a row.
 * If [useRow] is true, it will wrap the [content] in a row.
 * If it is false, adds [content] directly.
 *
 * [rowModifier] is only used if a row is used. Otherwise, it is ignored.
 *
 * This is used to help with responsive UI layout.
 */
@Composable
fun MaybeRow(
    useRow: Boolean = true,
    rowModifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable MaybeRowScope.() -> Unit
) {
    if (useRow) {
        Row(
            modifier = rowModifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
        ) {
            val scope = MaybeRowScopeWrapped(this)
            scope.content()
        }
    } else {
        MaybeRowScopeNoRow.content()
    }
}

/**
 * A [RowScope] that may or may not be inside an actual row.
 * Its methods will have no effect if we're not actually in a [RowScope],
 * except [weightOrFillMaxWidth], the effect of which differs depending
 * on whether we're in a row or not.
 */
interface MaybeRowScope: RowScope {
    /**
     * Convenience method to act as either [RowScope.weight] or [Modifier.fillMaxWidth],
     * depending on whether we're in an actual row.
     */
    fun Modifier.weightOrFillMaxWidth(weight: Float, fill: Boolean = true, fraction: Float = 1f): Modifier
}

private object MaybeRowScopeNoRow: MaybeRowScope {
    override fun Modifier.weightOrFillMaxWidth(weight: Float, fill: Boolean, fraction: Float) =
        fillMaxWidth(fraction)

    override fun Modifier.align(alignment: Alignment.Vertical) = this

    override fun Modifier.alignBy(alignmentLineBlock: (Measured) -> Int) = this

    override fun Modifier.alignBy(alignmentLine: HorizontalAlignmentLine) = this

    override fun Modifier.alignByBaseline() = this

    override fun Modifier.weight(weight: Float, fill: Boolean) = this
}

private class MaybeRowScopeWrapped(
    private val wrapped: RowScope
): MaybeRowScope, RowScope by wrapped {
    override fun Modifier.weightOrFillMaxWidth(weight: Float, fill: Boolean, fraction: Float) =
        weight(weight, fill)
}