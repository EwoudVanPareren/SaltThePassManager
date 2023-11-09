package nl.vanparerensoftwaredevelopment.saltthepassmanager.storage.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File

/**
 * Read an object from a JSON file.
 */
internal suspend fun <T> File.readJson(
    kSerializer: KSerializer<T>,
    json: Json = Json.Default
): T {
    return withContext(Dispatchers.IO) {
        json.decodeFromString(kSerializer, readText(Charsets.UTF_8))
    }
}

/**
 * Read an object from a JSON file.
 */
internal suspend inline fun <reified T> File.readJson(
    json: Json = Json.Default
): T = readJson(serializer<T>(), json)

/**
 * Write a value to a JSON file.
 */
internal suspend fun <T> File.writeJson(
    value: T,
    kSerializer: KSerializer<T>,
    json: Json = Json.Default
) {
    withContext(Dispatchers.IO) {
        parentFile?.mkdirs()
        writeText(json.encodeToString(kSerializer, value), Charsets.UTF_8)
    }
}

/**
 * Write a value to a JSON file.
 */
internal suspend inline fun <reified T> File.writeJson(
    value: T,
    json: Json = Json.Default
) = writeJson(value, serializer<T>(), json)
