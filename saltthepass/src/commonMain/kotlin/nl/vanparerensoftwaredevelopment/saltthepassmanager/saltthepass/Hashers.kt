package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass

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

    /**
     * Keccak512 is the same as "SHA-3" on the original SaltThePass.
     *
     * In reality, Keccak512 is more like a predecessor of SHA-3
     * that has been mixed up with the actual SHA-3 in various
     * sources around the internet.
     *
     * Due to this confusion, CryptoJS (the library used by
     * SaltThePass for its hashing algorithms) has its Keccak512
     * algorithm listed as SHA-3.
     */
    val keccak512 = Hasher("Keccak512", 86) {
        Keccak512().digest(it.encodeToByteArray())
    }

    val sha3 = Hasher("SHA-3", 86) {
        SHA3_512().digest(it.encodeToByteArray())
    }

    // TODO: Add RIPEMD-160 so it

    val default = keccak512

    val all = listOf(md5, sha1, sha2, sha3, keccak512)
    val byName = all.associateBy { it.name }
}