package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

internal fun <I, O> Flow<I>.mapToNewStateIn(scope: CoroutineScope, initValue: I, transform: (I) -> O) =
    map { transform(it!!) }.stateIn(scope, SharingStarted.Eagerly, transform(initValue))

internal fun <I, O> StateFlow<I>.mapToNewStateIn(scope: CoroutineScope, transform: (I) -> O) =
    map { transform(it!!) }.stateIn(scope, SharingStarted.Eagerly, transform(value))