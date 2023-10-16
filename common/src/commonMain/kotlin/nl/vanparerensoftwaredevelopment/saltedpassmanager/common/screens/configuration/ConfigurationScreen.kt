package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.configuration

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.Platform
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ext.nextEnumValue
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.ext.displayName
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.components.WithVerticalScrollbarOnDesktop
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR

/**
 * The screen where the user changes the app's configuration.
 */
@OptIn(ExperimentalMaterial3Api::class)
class ConfigurationScreen: Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ConfigurationScreenModel>()
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                val navigator = LocalNavigator.current!!
                TopAppBar(
                    title = { Text(stringResource(MR.strings.configs_title)) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) { Icon(AppIcons.ArrowBack, contentDescription = stringResource(MR.strings.generic_back)) }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                WithVerticalScrollbarOnDesktop(scrollState) {
                    Inner(
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
    private fun Inner(
        screenModel: ConfigurationScreenModel,
        modifier: Modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
        ) {
            Box {
                var isOpen by remember { mutableStateOf(false) }
                val currentTheme = screenModel.theme

                ListItem(
                    headlineContent = { Text(stringResource(MR.strings.config_theme_label)) },
                    supportingContent = { Text(currentTheme.displayName()) },
                    trailingContent = {
                        Icon(AppIcons.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        isOpen = !isOpen
                    }
                )
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (theme in Configuration.Theme.values()) {
                        if (theme == Configuration.Theme.SYSTEM && !Platform.isAndroid) {
                            break
                        }
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = theme.displayName(),
                                    fontWeight = if (theme == currentTheme) {
                                        FontWeight.Bold
                                    } else null
                                )
                            },
                            onClick = {
                                screenModel.changeTheme(theme)
                                isOpen = false
                            }
                        )
                    }
                }
            }

            if (screenModel.showTrayOptions) {
                ListItem(
                    headlineContent = { Text(stringResource(MR.strings.config_close_to_tray_label)) },
                    supportingContent = { Text(stringResource(MR.strings.config_close_to_tray_description)) },
                    trailingContent = {
                        Checkbox(
                            checked = screenModel.closeToTray,
                            onCheckedChange = { screenModel.changeCloseToTray(it) }
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text(stringResource(MR.strings.config_tray_icon_color)) },
                    supportingContent = {
                        Text(screenModel.trayIconColor.displayName())
                    },
                    trailingContent = {
                        val current = screenModel.trayIconColor
                        IconButton(
                            onClick = {
                                screenModel.changeTrayColor(current.nextEnumValue())
                            }
                        ) {
                            val icon = when (current) {
                                Configuration.TrayIconColor.LIGHT -> AppIcons.BrightnessHigh
                                Configuration.TrayIconColor.DARK -> AppIcons.BrightnessLow
                            }
                            Icon(icon, contentDescription = null)
                        }
                    }
                )
            }
            Box {
                var isOpen by remember { mutableStateOf(false) }
                val currentDensity = screenModel.density

                ListItem(
                    headlineContent = { Text(stringResource(MR.strings.config_density_label)) },
                    supportingContent = { Text(currentDensity.displayName()) },
                    trailingContent = {
                        Icon(AppIcons.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        isOpen = !isOpen
                    }
                )
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = { isOpen = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (density in Configuration.DensityLevel.values()) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = density.displayName(),
                                    fontWeight = if (density == currentDensity) {
                                        FontWeight.Bold
                                    } else null
                                )
                            },
                            onClick = {
                                screenModel.changeDensity(density)
                                isOpen = false
                            }
                        )
                    }
                }
            }
        }
    }
}