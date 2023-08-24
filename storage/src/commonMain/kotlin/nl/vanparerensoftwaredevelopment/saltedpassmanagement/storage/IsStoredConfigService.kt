package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

/**
 */
interface IsStoredConfigService {
    /**
     * This assumes that the amount of configurations will be small enough to fit
     * into memory. Considering the use case, this would be a safe assumption.
     */
    suspend fun listAllConfigs(): PersistentList<StoredConfig>
    suspend fun getDomainsFor(domainStart: String): PersistentList<String>
    suspend fun getConfigsFor(domainName: String): PersistentList<StoredConfig>

    suspend fun modify(operations: List<ModifyOperation>)

    suspend fun modify(block: ModifyOperation.OperationDSL.() -> Unit) {
        val list = mutableListOf<ModifyOperation>()
        ModifyOperation.OperationDSL(list).apply(block)
        modify(list.toList())
    }

    suspend fun add(item: StoredConfig) = modify {
        add(item, onSameKey = ModifyOperation.Add.OnSameKey.ERROR)
    }

    suspend fun addOrUpdate(item: StoredConfig) = modify {
        add(item, onSameKey = ModifyOperation.Add.OnSameKey.OVERWRITE)
    }

    suspend fun remove(key: StoredConfig.Key, ignoreMissing: Boolean = false) = modify {
        remove(key, ignoreMissing)
    }

    @Serializable
    sealed interface ModifyOperation {
        @Serializable
        data class Add(val add: List<StoredConfig>, val onSameKey: OnSameKey = OnSameKey.ERROR): ModifyOperation {
            @Serializable
            enum class OnSameKey {
                ERROR, IGNORE, OVERWRITE
            }
        }
        @Serializable
        data class Remove(val remove: List<StoredConfig.Key>, val ignoreMissing: Boolean = false): ModifyOperation

        class OperationDSL(private val list: MutableList<ModifyOperation>) {
            fun add(items: List<StoredConfig>, onSameKey: Add.OnSameKey = Add.OnSameKey.ERROR) {
                list += Add(items, onSameKey)
            }
            fun add(item: StoredConfig, onSameKey: Add.OnSameKey = Add.OnSameKey.ERROR) = add(listOf(item), onSameKey)
            fun remove(items: List<StoredConfig.Key>, ignoreMissing: Boolean = false) {
                list += Remove(items, ignoreMissing)
            }
            fun remove(item: StoredConfig.Key, ignoreMissing: Boolean = false) = remove(listOf(item), ignoreMissing)
        }
    }
}