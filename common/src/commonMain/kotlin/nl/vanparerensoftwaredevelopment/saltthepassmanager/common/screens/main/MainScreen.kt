package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
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
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.about.AboutScreen
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.configuration.ConfigurationScreen
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.windowsize.WindowWidthClass
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR
import nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass.Hashers
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.components.*
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ui.windowsize.LocalWindowSize
import kotlin.math.roundToInt

/**
 * The main screen of the application with the salted password generator.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
class MainScreen: Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<MainScreenModel>()
        val scrollState = rememberScrollState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(MR.strings.app_title)) },
                    actions = {
                        val navigator = LocalNavigator.current!!
                        IconButton(
                            onClick = {
                                navigator.push(ConfigurationScreen())
                            }
                        ) {
                            Icon(AppIcons.Settings, contentDescription = stringResource(MR.strings.main_screen_settings_tooltip))
                        }
                        IconButton(
                            onClick = {
                                navigator.push(AboutScreen())
                            }
                        ) {
                            Icon(AppIcons.Info, contentDescription = stringResource(MR.strings.main_screen_about_tooltip))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                WithVerticalScrollbarOnDesktop(scrollState) {
                    Form(
                        screenModel,
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(end = 10.dp)
                    )
                }
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

        var deletePopup by remember { mutableStateOf(false) }

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

                var textFieldWidth by remember { mutableStateOf(0) }

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
                                    contentDescription = stringResource(MR.strings.domain_suggestions_open_tooltip)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .onSizeChanged { textFieldWidth = it.width }
                )
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false },
                    modifier = Modifier.widthIn(min = textFieldWidth.fromPixelsToDp())
                ) {
                    suggestions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.domainName) },
                            onClick = {
                                screenModel.clickedDomainNameSuggestion(item)
                                isOpen = false
                            },
                            modifier = Modifier.widthIn(min = 500.dp)
                        )
                    }
                }
            }

            Box {
                var isSuggestionsOpen by remember { mutableStateOf(false) }
                val suggestions = screenModel.domainPhraseSuggestions
                var textFieldWidth by remember { mutableStateOf(0) }

                FormTextField(
                    label = stringResource(MR.strings.domain_phrase_label),
                    value = screenModel.domainPhrase,
                    onValueChange = { screenModel.domainPhrase = it },
                    trailingIcon = {
                        IconsRow {
                            HelpDot(stringResource(MR.strings.domain_phrase_help))

                            IconButton(
                                onClick = { isSuggestionsOpen = !isSuggestionsOpen },
                                enabled = screenModel.domainName.isNotEmpty()
                            ) {
                                Icon(
                                    AppIcons.ArrowDropDown,
                                    contentDescription = stringResource(MR.strings.domain_phrase_suggestions_open_tooltip)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { textFieldWidth = it.width }
                )
                DropdownMenu(
                    expanded = isSuggestionsOpen,
                    onDismissRequest = { isSuggestionsOpen = false },
                    modifier = Modifier.widthIn(min = textFieldWidth.fromPixelsToDp())
                ) {
                    suggestions.forEach { item ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(AppIcons.AccountCircle, contentDescription = null)
                            },
                            text = {
                                Text(buildAnnotatedString {
                                    if (item.domainPhrase.isEmpty()) {
                                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                            append(stringResource(MR.strings.account_default_label))
                                        }
                                    } else {
                                        append(item.domainPhrase)
                                    }
                                })
                            },
                            onClick = {
                                screenModel.clickedDomainPhraseSuggestion(item)
                                isSuggestionsOpen = false
                            }
                        )
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    enabled = screenModel.canDelete,
                    onClick = { deletePopup = true }
                ) {
                    Icon(
                        AppIcons.Delete,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(stringResource(MR.strings.account_delete_action_label))
                }
                TextButton(
                    enabled = screenModel.canSave,
                    onClick = { screenModel.saveCurrent() }
                ) {
                    Icon(
                        AppIcons.Save,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(stringResource(MR.strings.account_save_action_label))
                }
                HelpDot(text = stringResource(MR.strings.accounts_help))
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
                trailingIcon = {
                    HelpDot(stringResource(
                        if (showAdvanced) MR.strings.salted_password_help_advanced
                        else MR.strings.salted_password_help_basic
                    ))
                },
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
                    enabled = screenModel.canFormBeCleared
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

                FlowRow {
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
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

                    HelpDot(
                        text = stringResource(MR.strings.hashing_function_help),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    if (screenModel.hasher == Hashers.sha3) {
                        HelpDot(
                            icon = AppIcons.Warning,
                            iconColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(stringResource(MR.strings.hashing_function_sha3_note))
                        }
                    }
                }
            }
        }
        if (deletePopup) {
            AlertDialog(
                text = {
                    if (LocalWindowSize.current.width > WindowWidthClass.SMALL) {
                        Text(buildAnnotatedString {
                            val domainName = screenModel.domainName
                            val domainPhrase = screenModel.domainPhrase

                            append(stringResource(MR.strings.delete_account_long_message_1))
                            append("\n")

                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.error
                                )
                            ) {
                                append(domainName)
                            }
                            append(" - ")
                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold.takeIf { domainPhrase.isEmpty() },
                                    color = MaterialTheme.colorScheme.error
                                )
                            ) {
                                append(domainPhrase.ifEmpty {
                                    stringResource(MR.strings.account_default_label)
                                })
                            }
                            append("\n")
                            append(stringResource(MR.strings.delete_account_long_message_2))
                        })
                    } else {
                        Text(stringResource(MR.strings.delete_account_short_message))
                    }
                },
                onDismissRequest = { deletePopup = false },
                dismissButton = {
                    TextButton(
                        onClick = { deletePopup = false }
                    ) { Text(stringResource(MR.strings.delete_account_cancel)) }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (screenModel.canDelete) {
                                screenModel.deleteCurrent()
                            }
                            deletePopup = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text(stringResource(MR.strings.delete_account_confirm)) }
                },
                icon = { Icon(
                    AppIcons.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                ) }
            )
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

    @Composable
    private fun Int.fromPixelsToDp() = with (LocalDensity.current) {
        this@fromPixelsToDp.toDp()
    }
}
