package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main.MainScreen
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.theme.SaltedPassManagementTheme

@Composable
fun App() {
    SaltedPassManagementTheme {
        Navigator(MainScreen())
    }
}

