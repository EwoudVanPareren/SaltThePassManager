package nl.vanparerensoftwaredevelopment.saltthepassmanager.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.main.MainScreen
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.theme.SaltThePassManagerTheme

/**
 * The root composable for the SaltThePass Manager application.
 */
@Composable
fun SaltThePassManagerRoot() {
    SaltThePassManagerTheme {
        Navigator(MainScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}

