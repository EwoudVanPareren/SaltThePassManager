package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.lowercaseUS

/**
 * Stored account details.
 *
 * Everything but the master password is stored in this, allowing
 * the user to easily recall account-specific details.
 */
@Serializable
data class StoredAccount(
    val domainName: String,
    val domainPhrase: String,
    val versionName: String,
    val appendSpecial: String,
    val hasher: String,
    val length: Int,
) {

    /**
     * The identifying key for the given account,
     * which should help with avoiding duplicate entries.
     */
    @Transient
    val key = Key(domainName, domainPhrase)

    /**
     * A key type that uniquely identifies a [StoredAccount] in the storage system.
     */
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