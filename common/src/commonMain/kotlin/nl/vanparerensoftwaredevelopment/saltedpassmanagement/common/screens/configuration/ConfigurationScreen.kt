package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.configuration

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.resources.compose.stringResource
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.Platform
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.AppIcons
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.components.displayName
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.resources.MR

@OptIn(ExperimentalMaterial3Api::class)
class ConfigurationScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ConfigurationScreenModel>()

        val scrollState = rememberScrollState()
        val adapter = rememberScrollbarAdapter(scrollState)

        Scaffold(
            topBar = {
                val navigator = LocalNavigator.current!!
                TopAppBar(
                    title = { Text("Configuration") },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() }
                        ) { Icon(AppIcons.ArrowBack, contentDescription = "") }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Inner(
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
    fun Inner(
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
                    headlineText = { Text(stringResource(MR.strings.config_theme_label)) },
                    supportingText = { Text(currentTheme.displayName()) },
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
            Box {
                var isOpen by remember { mutableStateOf(false) }
                val currentDensity = screenModel.density

                ListItem(
                    headlineText = { Text(stringResource(MR.strings.config_density_label)) },
                    supportingText = { Text(currentDensity.displayName()) },
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