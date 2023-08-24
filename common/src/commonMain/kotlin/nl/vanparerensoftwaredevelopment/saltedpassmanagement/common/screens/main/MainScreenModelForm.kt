package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.saltthepass.Hashers

/**
 * Separate class that contains the form element states
 * in the main screen.
 *
 * This is deliberately kept in a separate class, to be
 * used as a singleton to share between [MainScreenModel]s.
 * This way, the form contents will not clear when navigating
 * away from [MainScreen], or when moving the application to
 * tray on desktop.
 */
class MainScreenModelForm {
    var masterPassword by mutableStateOf("")
    var domainName by mutableStateOf("")
    var domainPhrase by mutableStateOf("")
    var versionName by mutableStateOf("")
    var appendSpecial by mutableStateOf("")

    var length by mutableStateOf(20)
    var hasher by mutableStateOf(Hashers.default)
}