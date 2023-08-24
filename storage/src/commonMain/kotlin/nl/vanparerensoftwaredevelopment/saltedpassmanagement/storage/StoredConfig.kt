package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext.lowercaseUS

@Serializable
data class StoredConfig(
    val domainName: String,
    val domainPhrase: String,
    val versionName: String,
    val appendSpecial: String,
    val hasher: String,
    val length: Int,
) {

    /**
     * The identifying key for the given config,
     * which should help with avoiding duplicate entries.
     */
    @Transient
    val key = Key(domainName, domainPhrase)

    @Serializable
    data class Key private constructor(
        val domainName: String,
        val domainPhrase: String
    ) {
        companion object {
            operator fun invoke(domainName: String, domainPhrase: String) =
                Key(domainName.lowercaseUS(), domainPhrase.lowercaseUS())
        }
    }
}