package nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage

import kotlinx.coroutines.CoroutineScope
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.storage.file.FileStoredConfigService
import java.nio.file.Path

/**
 * Object class that provides access to stored configuration types.
 */
object StoredConfigServices {
    fun file(path: Path, scope: CoroutineScope): IsStoredConfigService =
        FileStoredConfigService(path, scope)
}
