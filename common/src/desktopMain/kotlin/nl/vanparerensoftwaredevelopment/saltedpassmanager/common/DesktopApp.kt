package nl.vanparerensoftwaredevelopment.saltedpassmanager.common

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.*
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.config.LocalAppConfiguration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.di.commonModule
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.di.desktopModule
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.TopLevelLocalProviders
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ui.WindowLevelLocalProviders
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR
import org.kodein.di.DI

/**
 * The main entry point for the desktop app.
 */
fun desktopMain(args: List<String>, openMessages: Flow<Unit>) {
    runBlocking {
        val di = DI {
            import(commonModule)
            import(desktopModule)
        }

        awaitApplication {
            var minimizedToTray by remember { mutableStateOf(false) }

            LaunchedEffect(null) {
                openMessages.onEach {
                    minimizedToTray = false
                }.collect()
            }

            TopLevelLocalProviders(di) {
                val appIcon = painterResource(MR.images.icon_application)

                val windowState = rememberWindowState()
                if (!Platform.isTraySupported || !minimizedToTray) {
                    MainWindow(
                        windowState,
                        appIcon,
                        minimizeToTray = { minimizedToTray = true }
                    )
                }

                if (Platform.isTraySupported) {
                    MainTray(
                        minimizedToTray,
                        toggleMinimized = { minimizedToTray = !minimizedToTray }
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationScope.MainWindow(
    windowState: WindowState,
    appIcon: Painter,
    minimizeToTray: () -> Unit
) {
    val closeToTray = LocalAppConfiguration.current.closeToTray
    Window(
        title = stringResource(MR.strings.app_title),
        onCloseRequest = {
            if (closeToTray) {
                minimizeToTray()
            } else {
                exitApplication()
            }
        },
        icon = appIcon,
        state = windowState
    ) {
        WindowLevelLocalProviders(windowState.size) {
            SaltThePassManagerRoot()
        }
    }
}

@Composable
private fun ApplicationScope.MainTray(
    isMinimized: Boolean,
    toggleMinimized: () -> Unit
) {
    val trayState = rememberTrayState()
    Tray(
        menu = {
            Item(
                text = stringResource(
                    if (isMinimized)
                        MR.strings.tray_menu_item_open_label
                    else
                        MR.strings.tray_menu_item_minimize_to_tray
                )
            ) {
                toggleMinimized()
            }
            Item(stringResource(MR.strings.tray_menu_item_close)) {
                exitApplication()
            }
        },
        state = trayState,
        icon = painterResource(
            when (LocalAppConfiguration.current.trayIconColor) {
                Configuration.TrayIconColor.LIGHT ->  MR.images.icon_tray_light
                Configuration.TrayIconColor.DARK ->  MR.images.icon_tray_dark
            }
        ),
        tooltip = stringResource(MR.strings.app_title),
        onAction = {
            toggleMinimized()
        }
    )
}