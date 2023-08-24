package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun FormDropdownButton(
    label: (@Composable () -> Unit)? = null,
    isOpen: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedFormItemContainer(
        label = label,
        overrideFocusVisualState = if (isOpen) true else null,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                    content()
                }
            }
            Icon(Icons.Default.ArrowDropDown, null)
        }
    }
}