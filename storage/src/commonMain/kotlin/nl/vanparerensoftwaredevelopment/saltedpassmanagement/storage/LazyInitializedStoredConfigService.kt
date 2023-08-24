package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class LazyInitializedStoredConfigService(
    private val createService: suspend () -> IsStoredConfigService
): IsStoredConfigService {
    private lateinit var loadedService: IsStoredConfigService
    private val initMutex = Mutex()

    override suspend fun listAllConfigs() = getService().listAllConfigs()

    override suspend fun getDomainsFor(domainStart: String) =
        getService().getDomainsFor(domainStart)

    override suspend fun getConfigsFor(domainName: String) =
        getService().getConfigsFor(domainName)

    override suspend fun modify(operations: List<IsStoredConfigService.ModifyOperation>) =
        getService().modify(operations)

    private suspend fun getService(): IsStoredConfigService {
        if (!::loadedService.isInitialized) {
            initMutex.withLock {
                if (!::loadedService.isInitialized) {
                    loadedService = createService()
                }
            }
        }
        return loadedService
    }
}