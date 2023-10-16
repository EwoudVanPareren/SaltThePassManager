package nl.vanparerensoftwaredevelopment.saltedpassmanager.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.screens.main.MainScreen
import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.theme.SaltThePassManagerTheme

/**
 * The root composable for the SaltThePass Manager application.
 */
@Composable
fun SaltThePassManagerRoot() {
    SaltThePassManagerTheme {
        Navigator(MainScreen())
    }
}

