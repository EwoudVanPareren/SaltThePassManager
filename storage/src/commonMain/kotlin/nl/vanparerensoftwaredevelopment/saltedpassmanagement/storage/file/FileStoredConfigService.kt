package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.file

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext.lowercaseUS
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.StoredConfig
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.IsStoredConfigService
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.LazyInitializedStoredConfigService
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext.associateByMulti
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.ext.mapToNewStateIn
import java.nio.file.Path
import kotlin.math.abs

internal class FileStoredConfigService private constructor(
    private val store: KStore<StoredConfigsFile>,
    initValue: StoredConfigsFile,
    scope: CoroutineScope
): IsStoredConfigService, CoroutineScope by scope {

    /**
     * Using a persistent collection so that it doesn't need to be copied again on listAllConfigs.
     */
    private val configs: StateFlow<PersistentList<StoredConfig>> =
        store.updates.mapToNewStateIn(this, initValue) { file ->
            file!!.items.sortedWith(
                Comparator.comparing { it: StoredConfig ->
                    it.domainName.lowercaseUS()
                }.thenComparing { it: StoredConfig ->
                    it.domainPhrase.lowercaseUS()
                }
            ).toPersistentList()
        }

    private val byDomain = configs.mapToNewStateIn(this) { list ->
        list.associateByMulti { it.domainName.lowercaseUS() }
    }

    override suspend fun listAllConfigs(): PersistentList<StoredConfig> = configs.value

    override suspend fun getDomainsFor(domainStart: String): PersistentList<String> {
        return if (domainStart.isNotEmpty()) {
            byDomain.value.filterKeys {
                it.startsWith(domainStart, ignoreCase = true)
            }.values.map {
                it.first().domainName
            }.sorted().sortedByDescending {
                abs(it.length - domainStart.length)
            }
        } else {
            byDomain.value.keys.sorted()
        }.toPersistentList()
    }

    override suspend fun getConfigsFor(domainName: String): PersistentList<StoredConfig> {
        return byDomain.value[domainName.lowercaseUS()].orEmpty().toPersistentList()
    }

    override suspend fun modify(operations: List<IsStoredConfigService.ModifyOperation>) {
        if (operations.isEmpty()) return

        store.update { stored ->
            val list = stored!!.items.toMutableList()
            operations.forEach { operation ->
                when (operation) {
                    is IsStoredConfigService.ModifyOperation.Add -> {
                        val addByKey = operation.add.associateBy { it.key }.toMutableMap()
                        list.forEachIndexed { index, item ->
                            val newerItemWithSameKey = addByKey[item.key]
                            if (newerItemWithSameKey != null) {
                                addByKey.remove(item.key)
                                when (operation.onSameKey) {
                                    IsStoredConfigService.ModifyOperation.Add.OnSameKey.ERROR -> {
                                        throw IllegalArgumentException("Failed to add: a configuration with the key combination ${item.key} already exists")
                                    }
                                    IsStoredConfigService.ModifyOperation.Add.OnSameKey.IGNORE -> { }
                                    IsStoredConfigService.ModifyOperation.Add.OnSameKey.OVERWRITE -> {
                                        list[index] = newerItemWithSameKey
                                    }
                                }
                            }
                        }
                        list += addByKey.values
                    }
                    is IsStoredConfigService.ModifyOperation.Remove -> {
                        val toRemove = operation.remove.toMutableSet()
                        val iterator = list.listIterator()
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            if (item.key in toRemove) {
                                println("Removing ${item.key}...")
                                iterator.remove()
                                toRemove -= item.key
                            } else {
                                println("Passing by ${item.key}...")
                            }
                        }
                        if (toRemove.isNotEmpty() && !operation.ignoreMissing) {
                            throw IllegalArgumentException("Failed to remove: no configuration found for key ${toRemove.first()}")
                        }
                    }
                }
            }
            stored.copy(items = list.toList())
        }
    }

    companion object {
        operator fun invoke(storagePath: Path, serviceScope: CoroutineScope): IsStoredConfigService =
            LazyInitializedStoredConfigService {
                val store: KStore<StoredConfigsFile> = storeOf(
                    filePath = storagePath.toFile().path,
                    default = StoredConfigsFile(emptyList())
                )
                val initValue = store.get()!!

                FileStoredConfigService(store, initValue, serviceScope)
            }
    }
}