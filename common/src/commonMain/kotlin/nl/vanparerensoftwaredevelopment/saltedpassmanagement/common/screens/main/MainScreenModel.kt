package nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.saltthepass.Hashers
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.saltthepass.SaltThePass
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.IsStoredConfigService
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.StoredConfig

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenModel(
    private val storedConfigService: IsStoredConfigService,
    private val modelForm: MainScreenModelForm
): ScreenModel {
    private val updateMutex = Mutex()

    var masterPassword by modelForm::masterPassword
    var domainName by modelForm::domainName
    var domainPhrase by modelForm::domainPhrase
    var versionName by modelForm::versionName
    var appendSpecial by modelForm::appendSpecial

    var length by modelForm::length
    var hasher by modelForm::hasher

    var domainNameSuggestions by mutableStateOf<List<String>>(emptyList())
    var domainPhraseSuggestions by mutableStateOf<List<StoredConfig>>(emptyList())

    var output by mutableStateOf("")
        private set

    var copiedToClipboard by mutableStateOf(false)

    var canBeCleared by mutableStateOf(false)

    var canSave by mutableStateOf(false)
    var canUpdate by mutableStateOf(false)
    var canDelete by mutableStateOf(false)

    var isUpdatingStorage by mutableStateOf(false)

    init {
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
        }.launchIn(coroutineScope)

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
        }.launchIn(coroutineScope)

        snapshotFlow {
            domainName.isNotEmpty()
                    || domainPhrase.isNotEmpty()
                    || versionName.isNotEmpty()
                    || appendSpecial.isNotEmpty()
                    || length != 20
                    || hasher != Hashers.default
        }.onEach {
            canBeCleared = it
        }.launchIn(coroutineScope)

        snapshotFlow {
            domainName
        }.mapLatest { start ->
            val domains = storedConfigService.getDomainsFor(start)
            val phrases = if (start.isEmpty()) {
                emptyList()
            } else {
                storedConfigService.getConfigsFor(start)
            }
            domains to phrases
        }.onEach { (domains, configs) ->
            domainNameSuggestions = domains
            domainPhraseSuggestions = configs
        }.launchIn(coroutineScope)

        snapshotFlow {
            StoredConfig(
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
            when {
                found == null -> CurrentStoredState.NO_MATCH
                found == current -> CurrentStoredState.FULL_MATCH
                else -> CurrentStoredState.KEY_MATCH
            }
        }.onEach {
            canSave = it == CurrentStoredState.NO_MATCH
            canUpdate = it == CurrentStoredState.KEY_MATCH
            canDelete = it == CurrentStoredState.KEY_MATCH
                    || it == CurrentStoredState.FULL_MATCH
        }.launchIn(coroutineScope)
    }

    fun clickedDomainNameSuggestion(suggested: String) {
        domainName = suggested
    }

    fun clickedDomainPhraseSuggestion(suggested: StoredConfig) {
        domainName = suggested.domainName
        domainPhrase = suggested.domainPhrase
        versionName = suggested.versionName
        appendSpecial = suggested.appendSpecial
        length = suggested.length
        Hashers.byName[suggested.hasher]?.let {
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
        updateStorage {
            storedConfigService.addOrUpdate(
                StoredConfig(
                    domainName = domainName,
                    domainPhrase = domainPhrase,
                    versionName = versionName,
                    appendSpecial = appendSpecial,
                    hasher = hasher.name,
                    length = length
                )
            )
        }
    }

    fun deleteCurrent() {
        updateStorage {
            storedConfigService.remove(
                StoredConfig.Key(
                    domainName = domainName,
                    domainPhrase = domainPhrase
                )
            )
        }
    }

    private fun updateStorage(block: suspend () -> Unit) {
        coroutineScope.launch {
            updateMutex.withLock {
                isUpdatingStorage = true
                try {
                    block()
                    domainPhraseSuggestions = storedConfigService.getConfigsFor(domainName)
                } finally {
                    isUpdatingStorage = false
                }
            }
        }
    }

    fun clearForm() {
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