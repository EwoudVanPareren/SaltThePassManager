package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.configuration.ConfigurationScreen
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components.*
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize.WindowWidthClass
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.resources.MR
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.saltthepass.Hashers
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
class MainScreen: Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<MainScreenModel>()

        val scrollState = rememberScrollState()
        val adapter = rememberScrollbarAdapter(scrollState)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SaltThePass Manager") },
                    actions = {
                        val navigator = LocalNavigator.current!!
                        IconButton(
                            onClick = {
                                navigator.push(ConfigurationScreen())
                            },
                        ) {
                            Icon(AppIcons.Settings, contentDescription = "Settings")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Form(
                    screenModel,
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(end = 10.dp)
                )
                VerticalScrollbar(
                    adapter,
                    modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight()
                )
            }
        }
    }

    @Composable
    fun Form(
        screenModel: MainScreenModel,
        modifier: Modifier = Modifier
    ) {
        val windowWidthClass = WindowWidthClass.current
        var showAdvancedOptionsChecked by remember { mutableStateOf(false) }

        val showAdvancedAnyway = screenModel.versionName.isNotEmpty()
                || screenModel.appendSpecial.isNotEmpty()
                || screenModel.hasher != Hashers.default

        val showAdvanced = showAdvancedOptionsChecked || showAdvancedAnyway

        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            FormTextField(
                label = stringResource(MR.strings.master_password_label),
                value = screenModel.masterPassword,
                onValueChange = { screenModel.masterPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = { HelpDot(stringResource(MR.strings.master_password_help)) },
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                var isOpen by remember { mutableStateOf(false) }
                val suggestions = screenModel.domainNameSuggestions
                FormTextField(
                    label = stringResource(MR.strings.domain_name_label),
                    value = screenModel.domainName,
                    onValueChange = { screenModel.domainName = it },
                    trailingIcon = {
                        IconsRow {
                            HelpDot(stringResource(MR.strings.domain_name_help))
                            IconToggleButton(
                                checked = isOpen,
                                onCheckedChange = { isOpen = it },
                                enabled = suggestions.isNotEmpty()
                            ) {
                                Icon(
                                    AppIcons.ArrowDropDown,
                                    contentDescription = "Open suggestions..."
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false }
                ) {
                    suggestions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                screenModel.clickedDomainNameSuggestion(item)
                                isOpen = false
                            },
                            modifier = Modifier.widthIn(min = 500.dp)
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                var isOpen by remember { mutableStateOf(false) }
                val suggestions = screenModel.domainPhraseSuggestions
                FormTextField(
                    label = stringResource(MR.strings.domain_phrase_label),
                    value = screenModel.domainPhrase,
                    onValueChange = { screenModel.domainPhrase = it },
                    trailingIcon = {
                        IconsRow {
                            HelpDot(stringResource(MR.strings.domain_phrase_help))

                            IconButton(
                                onClick = { isOpen = !isOpen },
                                enabled = screenModel.domainName.isNotEmpty()
                            ) {
                                Icon(
                                    AppIcons.ArrowDropDown,
                                    contentDescription = "Open suggestions..."
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val curPhrase = screenModel.domainPhrase
                    if (screenModel.canSave || screenModel.canUpdate) {
                        val isUpdate = screenModel.canUpdate
                        DropdownMenuItem(
                            leadingIcon = {
                                val icon = if (isUpdate) {
                                    AppIcons.Edit
                                } else {
                                    AppIcons.AddCircle
                                }
                                Icon(icon, contentDescription = null)
                            },
                            text = {
                                Text(buildAnnotatedString {
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                        if (screenModel.canUpdate) {
                                            append("Update")
                                        } else {
                                            append("Save")
                                        }
                                        append(" ")
                                    }
                                    appendDomainPhrase(curPhrase)
                                })
                            },
                            onClick = {
                                screenModel.saveCurrent()
                                isOpen = false
                            }
                        )
                    }
                    if (screenModel.canDelete) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(AppIcons.Delete, contentDescription = null)
                            },
                            text = {
                                Text(buildAnnotatedString {
                                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                        append("Delete ")
                                    }
                                    appendDomainPhrase(curPhrase)
                                })
                            },
                            onClick = {
                                screenModel.deleteCurrent()
                                isOpen = false
                            },
                            colors = MenuDefaults.itemColors(leadingIconColor = MaterialTheme.colorScheme.error)
                        )
                    }
                    suggestions.forEach { item ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(AppIcons.AccountCircle, contentDescription = null)
                            },
                            text = {
                                Text(buildAnnotatedString {
                                    if (item.domainPhrase.isEmpty()) {
                                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                            append("Default (no text)")
                                        }
                                    } else {
                                        append(item.domainPhrase)
                                    }
                                })
                            },
                            onClick = {
                                screenModel.clickedDomainPhraseSuggestion(item)
                                isOpen = false
                            }
                        )
                    }
                }
            }

            if (showAdvanced) {
                MaybeRow(
                    useRow = (windowWidthClass >= WindowWidthClass.MEDIUM),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FormTextField(
                        label = stringResource(MR.strings.version_name_label),
                        value = screenModel.versionName,
                        onValueChange = { screenModel.versionName = it },
                        trailingIcon = { HelpDot(stringResource(MR.strings.version_name_help)) },
                        modifier = Modifier.weightOrFillMaxWidth(weight = 1f)
                    )
                    FormTextField(
                        label = stringResource(MR.strings.password_appendix_label),
                        value = screenModel.appendSpecial,
                        onValueChange = { screenModel.appendSpecial = it },
                        trailingIcon = { HelpDot(stringResource(MR.strings.password_appendix_help)) },
                        modifier = Modifier.weightOrFillMaxWidth(weight = 1f)
                    )
                }
            }

            var lengthFocus by remember { mutableStateOf(false) }
            OutlinedFormItemContainer(
                label = { Text(stringResource(MR.strings.password_length_label)) },
                overrideFocusVisualState = lengthFocus,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Slider(
                            value = screenModel.length.toFloat(),
                            onValueChange = { screenModel.length = it.roundToInt() },
                            valueRange = 0.6f..(screenModel.hasher.maxLength.toFloat() + 0.4f),
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    lengthFocus = it.hasFocus
                                }
                        )
                        Text(
                            text = screenModel.length.toString(),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .widthIn(min = (LocalDensity.current.fontScale * 25).dp)
                        )
                        HelpDot(stringResource(MR.strings.password_length_help))
                    }
                }
            }

            FormTextField.ReadOnly(
                label = stringResource(MR.strings.salted_password_label),
                value = screenModel.output,
                trailingIcon = { HelpDot(stringResource(
                    if (showAdvanced) MR.strings.salted_password_help_advanced
                            else MR.strings.salted_password_help_basic
                )) },
                modifier = Modifier.fillMaxWidth()
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val clipboardManager = LocalClipboardManager.current
                Button(
                    onClick = {
                        screenModel.copyToClipboard(clipboardManager)
                    },
                    enabled = screenModel.output.isNotEmpty()
                ) {
                    Text(stringResource(MR.strings.copy_password_label))
                }
                Button(
                    onClick = {
                        screenModel.clearClipboard(clipboardManager)
                    },
                    enabled = screenModel.copiedToClipboard
                ) {
                    Text(stringResource(MR.strings.clear_clipboard_label))
                }
                Button(
                    onClick = { screenModel.clearForm() },
                    enabled = screenModel.canBeCleared
                ) {
                    Text(stringResource(MR.strings.clear_form_label))
                }
            }

            TextButton(
                onClick = { showAdvancedOptionsChecked = !showAdvancedOptionsChecked },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TriStateCheckbox(
                        state = when {
                            showAdvancedOptionsChecked -> ToggleableState.On
                            showAdvancedAnyway -> ToggleableState.Indeterminate
                            else -> ToggleableState.Off
                        },
                        onClick = null
                    )
                    Text(stringResource(MR.strings.advanced_settings_label))
                }
            }

            if (showAdvanced) {
                var hashMenuOpened by remember { mutableStateOf(false) }

                Box {
                    FormDropdownButton(
                        label = { Text(stringResource(MR.strings.hashing_function_label)) },
                        isOpen = hashMenuOpened,
                        onClick = { hashMenuOpened = !hashMenuOpened },
                        modifier = Modifier.widthIn(max = 250.dp)
                    ) {
                        Text(screenModel.hasher.name)
                    }
                    DropdownMenu(
                        expanded = hashMenuOpened,
                        onDismissRequest = { hashMenuOpened = false },
                        modifier = Modifier.width(250.dp)
                    ) {
                        for (hasher in Hashers.all) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = hasher.name,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                    )
                                },
                                onClick = {
                                    screenModel.hasher = hasher
                                    hashMenuOpened = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private inline fun IconsRow(
        content: @Composable RowScope.() -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }

    private fun AnnotatedString.Builder.appendDomainPhrase(domainPhrase: String) {
        if (domainPhrase.isEmpty()) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Default (empty)")
            }
        } else {
            append(domainPhrase)
        }
    }

}