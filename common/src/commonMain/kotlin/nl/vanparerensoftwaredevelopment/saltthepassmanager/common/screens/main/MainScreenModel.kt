package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.screens.main

import androidx.compose.runtime.*
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nl.vanparerensoftwaredevelopment.saltthepassmanager.common.config.Configuration
import nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass.Hashers
import nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass.SaltThePass
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.StoredAccountsService.AddOnSameKey
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.DomainSuggestion
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model.StoredAccount
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.store.DataStore

/**
 * The screen model (/viewmodel) for [MainScreen].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenModel(
    appConfig: DataStore<Configuration>,
    private val storedAccountsService: StoredAccountsService,
    private val modelForm: MainScreenModelForm
): ScreenModel {
    private var config by mutableStateOf(Configuration.DEFAULT)

    var masterPassword by modelForm::masterPassword
    var domainName by modelForm::domainName
    var domainPhrase by modelForm::domainPhrase
    var versionName by modelForm::versionName
    var appendSpecial by modelForm::appendSpecial

    var length by modelForm::length
    var hasher by modelForm::hasher

    var domainNameSuggestions by mutableStateOf<List<DomainSuggestion>>(emptyList())
    var domainPhraseSuggestions by mutableStateOf<List<StoredAccount>>(emptyList())

    var output by mutableStateOf("")
        private set

    var copiedToClipboard by mutableStateOf(false)

    var canFormBeCleared by mutableStateOf(false)

    var canSave by mutableStateOf(false)
    var canDelete by mutableStateOf(false)

    var isCurrentAccountNew by mutableStateOf(true)

    init {
        appConfig.updates.onEach {
            config = it
        }.launchIn(screenModelScope)

        // Ensure that length doesn't exceed the hasher's max
        snapshotFlow {
            try {
                hasher.maxLength.takeIf { length > it }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                null
            }
        }.filterNotNull().onEach {
            length = it
        }.launchIn(screenModelScope)

        snapshotFlow {
            try {
                val actualLength = length.coerceIn(1, hasher.maxLength)
                when {
                    masterPassword.isEmpty() -> ""
                    domainName.isEmpty() -> ""
                    else -> SaltThePass.hash(
                        masterPassword,
                        domainName, domainPhrase, versionName,
                        actualLength, hasher,
                        appendSpecial
                    )
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                ""
            }
        }.onEach {
            output = it
        }.launchIn(screenModelScope)

        snapshotFlow {
            (!config.keepMasterPasswordOnClearForm && masterPassword.isNotEmpty())
                    || domainName.isNotEmpty()
                    || domainPhrase.isNotEmpty()
                    || versionName.isNotEmpty()
                    || appendSpecial.isNotEmpty()
                    || length != 20
                    || hasher != Hashers.default
        }.onEach {
            canFormBeCleared = it
        }.launchIn(screenModelScope)

        val domainNames = snapshotFlow { domainName }

        domainNames.flatMapLatest { start ->
            storedAccountsService.suggestedDomainsFor(start)
        }.onEach {
            domainNameSuggestions = it
        }.launchIn(screenModelScope)

        domainNames.flatMapLatest { domain ->
            storedAccountsService.getAccountsFor(domain)
        }.onEach {
            domainPhraseSuggestions = it
        }.launchIn(screenModelScope)

        snapshotFlow {
            StoredAccount(
                domainName = domainName,
                domainPhrase = domainPhrase,
                versionName = versionName,
                appendSpecial = appendSpecial,
                hasher = hasher.name,
                length = length
            ) to domainPhraseSuggestions
        }.mapLatest { (current, saved) ->
            if (current.key.domainName.isEmpty()) {
                return@mapLatest CurrentStoredState.INCOMPLETE_KEY
            }

            val found = saved.firstOrNull {
                it.key == current.key
            }
            when (found) {
                null -> CurrentStoredState.NO_MATCH
                current -> CurrentStoredState.FULL_MATCH
                else -> CurrentStoredState.KEY_MATCH
            }
        }.onEach {
            canSave = it == CurrentStoredState.NO_MATCH
                    || it == CurrentStoredState.KEY_MATCH
            canDelete = it == CurrentStoredState.KEY_MATCH
                    || it == CurrentStoredState.FULL_MATCH
            isCurrentAccountNew = it == CurrentStoredState.NO_MATCH
                    || it == CurrentStoredState.INCOMPLETE_KEY
        }.launchIn(screenModelScope)
    }

    fun clickedDomainNameSuggestion(suggested: DomainSuggestion) {
        val defaultAccount = suggested.defaultAccount
        if (defaultAccount != null) {
            populateWithAccount(defaultAccount)
        } else {
            domainName = suggested.domainName
        }
    }

    fun clickedDomainPhraseSuggestion(suggested: StoredAccount) {
        populateWithAccount(suggested)
    }

    private fun populateWithAccount(account: StoredAccount) {
        domainName = account.domainName
        domainPhrase = account.domainPhrase
        versionName = account.versionName
        appendSpecial = account.appendSpecial
        length = account.length
        Hashers.byName[account.hasher]?.let {
            hasher = it
        }
    }

    fun copyToClipboard(clipboardManager: ClipboardManager) {
        clipboardManager.setText(AnnotatedString(output))
        copiedToClipboard = true
    }

    fun clearClipboard(clipboardManager: ClipboardManager) {
        if (clipboardManager.hasText()) {
            clipboardManager.setText(AnnotatedString(""))
        }
        copiedToClipboard = false
    }

    fun saveCurrent() {
        val item = StoredAccount(
            domainName = domainName,
            domainPhrase = domainPhrase,
            versionName = versionName,
            appendSpecial = appendSpecial,
            hasher = hasher.name,
            length = length
        )
        screenModelScope.launch {
            storedAccountsService.add(item, AddOnSameKey.OVERWRITE)
        }
    }

    fun deleteCurrent() {
        val key = StoredAccount.Key(
            domainName = domainName,
            domainPhrase = domainPhrase
        )
        screenModelScope.launch {
            storedAccountsService.remove(key, ignoreMissing = true)
        }
    }

    fun clearForm() {
        if (!config.keepMasterPasswordOnClearForm) {
            masterPassword = ""
        }
        domainName = ""
        domainPhrase = ""
        versionName = ""
        appendSpecial = ""
        length = 20
        hasher = Hashers.default
    }

    private enum class CurrentStoredState {
        INCOMPLETE_KEY, NO_MATCH, KEY_MATCH, FULL_MATCH
    }
}