package nl.vanparerensoftwaredevelopment.saltedpassmanagement.saltthepass

import org.kotlincrypto.hash.md.MD5
import org.kotlincrypto.hash.sha1.SHA1
import org.kotlincrypto.hash.sha2.SHA512
import org.kotlincrypto.hash.sha3.*

/**
 * Hashing functions that can be used for generating passwords.
 */
object Hashers {
    val md5 = Hasher("MD5", 20) {
        MD5().digest(it.encodeToByteArray())
    }
    val sha1 = Hasher("SHA-1", 26) {
        SHA1().digest(it.encodeToByteArray())
    }
    val sha2 = Hasher("SHA-2", 86) {
        SHA512().digest(it.encodeToByteArray())
    }
    val keccak = Hasher("Keccak512", 86) {
        Keccak512().digest(it.encodeToByteArray())
    }
    val sha3 = Hasher("SHA-3", 86) {
        SHA3_512().digest(it.encodeToByteArray())
    }

    val default = keccak

    val all = listOf(md5, sha1, sha2, sha3, keccak)
    val byName = all.associateBy { it.name }
}