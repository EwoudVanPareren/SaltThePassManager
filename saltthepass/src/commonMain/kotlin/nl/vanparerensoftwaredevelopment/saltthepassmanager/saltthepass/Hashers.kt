package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass

import org.bouncycastle.jcajce.provider.digest.*

/**
 * Hashing functions that can be used for generating passwords.
 */
object Hashers {
    val md5 = Hasher("MD5", 20) {
        MD5.Digest().digest(it.encodeToByteArray())
    }
    val sha1 = Hasher("SHA-1", 26) {
        SHA1.Digest().digest(it.encodeToByteArray())
    }
    val sha2 = Hasher("SHA-2", 86) {
        SHA512.Digest().digest(it.encodeToByteArray())
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
        Keccak.Digest512().digest(it.encodeToByteArray())
    }

    val sha3 = Hasher("SHA-3", 86) {
        SHA3.Digest512().digest(it.encodeToByteArray())
    }

    val ripemd160 = Hasher("RIPEMD-160", 27) {
        RIPEMD160.Digest().digest(it.encodeToByteArray())
    }

    val default = keccak512

    val all = listOf(md5, sha1, sha2, sha3, keccak512, ripemd160)
    val byName = all.associateBy { it.name }
}