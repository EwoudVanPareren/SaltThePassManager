@file:OptIn(ExperimentalMaterial3Api::class)

package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

object FormTextField {
    @Composable
    operator fun invoke(
        label: String? = null,
        value: String,
        onValueChange: (String) -> Unit,
        singleLine: Boolean = true,
        readOnly: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Next,
        trailingIcon: (@Composable () -> Unit)? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        modifier: Modifier = Modifier
    ) {
        OutlinedTextField(
            label = label?.let { { Text(it) } },
            value = value,
            singleLine = singleLine,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            onValueChange = onValueChange,
            modifier = modifier
        )
    }

    @Composable
    fun ReadOnly(
        label: String? = null,
        value: String,
        singleLine: Boolean = true,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Next,
        trailingIcon: (@Composable () -> Unit)? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        modifier: Modifier = Modifier
    ) {
        FormTextField(
            label = label,
            value = value,
            onValueChange = { },
            singleLine = singleLine,
            readOnly = true,
            keyboardType = keyboardType,
            visualTransformation = visualTransformation,
            imeAction = imeAction,
            trailingIcon = trailingIcon,
            modifier = modifier
        )
    }
}