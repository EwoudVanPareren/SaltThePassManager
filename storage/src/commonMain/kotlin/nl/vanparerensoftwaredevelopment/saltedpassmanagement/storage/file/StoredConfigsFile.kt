package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.file

import kotlinx.serialization.Serializable
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.StoredConfig

@Serializable
internal data class StoredConfigsFile(
    val items: List<StoredConfig>
)
