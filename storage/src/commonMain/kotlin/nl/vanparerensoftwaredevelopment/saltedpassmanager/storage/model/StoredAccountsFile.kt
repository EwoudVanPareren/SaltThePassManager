package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage.model

import kotlinx.serialization.Serializable

/**
 * The root content of the file containing stored accounts.
 */
@Serializable
internal data class StoredAccountsFile(
    val items: List<StoredAccount>
)
