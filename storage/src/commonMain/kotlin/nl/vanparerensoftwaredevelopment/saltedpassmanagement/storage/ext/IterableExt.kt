package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

internal inline fun <T, K> Iterable<T>.associateByMulti(toKey: (T) -> K): PersistentMap<K, PersistentList<T>> {
    val map = mutableMapOf<K, MutableList<T>>()
    forEach {
        map.getOrPut(toKey(it)) { mutableListOf() } += it
    }
    return map.mapValues { it.value.toPersistentList() }.toPersistentMap()
}