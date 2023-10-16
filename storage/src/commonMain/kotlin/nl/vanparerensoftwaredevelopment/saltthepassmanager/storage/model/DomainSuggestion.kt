package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext.lowercaseUS

/**
 * A suggestion for a domain name, including a possible default
 * account that can be set when the suggested name is selected.
 */
@Serializable
data class DomainSuggestion(
    val domainName: String,
    val defaultAccount: StoredAccount? = null
) {

    /**
     * The domain name in a lowercase form, which is useful
     * for sorting purposes.
     */
    @Transient
    val domainNameLowercase = domainName.lowercaseUS()
}