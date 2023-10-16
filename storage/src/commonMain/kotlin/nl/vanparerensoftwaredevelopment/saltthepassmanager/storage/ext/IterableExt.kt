package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext

/**
 * Returns a [Map] containing the elements from the given iterable indexed by the key
 * returned from [toKey] function applied to each element.
 *
 * The value of each entry in the [Map] is a list that contains each element from the
 * iterable that was associated with the entry's key.
 */
internal inline fun <T, K> Iterable<T>.associateByMulti(toKey: (T) -> K): Map<K, List<T>> {
    val map = mutableMapOf<K, MutableList<T>>()
    forEach {
        map.getOrPut(toKey(it)) { mutableListOf() } += it
    }
    return map.mapValues { it.value.toList() }
}

/**
 * Create a [Map] from the iterable that contains the amount of time
 * each element occurs in the iterable.
 */
internal fun <K> Iterable<K>.toCountMap(): Map<K, Int> {
    val map = mutableMapOf<K, Int>()
    forEach {
        map[it] = (map[it] ?: 0) + 1
    }
    return map.toMap()
}