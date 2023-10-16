package nl.vanparerensoftwaredevelopment.saltedpassmanager.common.ext

/**
 * Get the next enum value. If this is the last enum value,
 * wrap around and get the first value again.
 */
inline fun <reified T: Enum<T>> T.nextEnumValue(): T {
    val values = enumValues<T>()
    return values[(ordinal + 1) % values.size]
}