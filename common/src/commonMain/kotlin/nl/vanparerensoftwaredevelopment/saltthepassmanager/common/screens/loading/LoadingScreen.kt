package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.painterResource
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.LocalAppConfiguration
import nl.vanparerensoftwaredevelopment.saltedpassmanager.resources.MR

/**
 * A general purpose loading screen.
 *
 * This is not a Voyager-library style screen, but rather a screen-sized UI component.
 */
@Composable
fun LoadingScreen(overrideDarkMode: Boolean? = null) {
    Scaffold { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues).fillMaxSize(1f)
        ) {
            val useLightIcon = overrideDarkMode ?: when (LocalAppConfiguration.current.theme) {
                Configuration.Theme.LIGHT -> false
                Configuration.Theme.DARK -> true
                Configuration.Theme.SYSTEM -> isSystemInDarkTheme()
            }
            Image(
                painter = painterResource(if (useLightIcon) {
                    MR.images.icon_tray_light
                } else {
                    MR.images.icon_tray_dark
                }),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .aspectRatio(1f)
            )
        }
    }
}