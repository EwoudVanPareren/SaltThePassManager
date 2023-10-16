package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext

import java.util.*

/**
 * Convenience method for consistent lowercasing across different devices
 * with different locales.
 */
internal fun String.lowercaseUS() = lowercase(Locale.US)