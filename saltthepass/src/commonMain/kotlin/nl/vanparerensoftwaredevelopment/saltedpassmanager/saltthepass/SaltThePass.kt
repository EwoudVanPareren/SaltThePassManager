package nl.vanparerensoftwaredevelopment.saltedpassmanager.saltthepass

import korlibs.crypto.encoding.toBase64

/**
 * Access to the SaltThePass functionality.
 */
object SaltThePass {
    /**
     * Apply the SaltThePass hashing algorithm to
     * generate a password.
     */
    fun hash(
        masterPassword: String,
        domainName: String,
        domainPhrase: String,
        versionName: String = "",
        length: Int,
        hasher: Hasher,
        appendSpecial: String = ""
    ): String {
        check(length > 0) {
            "Length must be 1 or longer"
        }
        val realLength = length - appendSpecial.length
        check(realLength <= hasher.maxLength) {
            "Length ($realLength) too long for hashing function (max is ${hasher.maxLength})"
        }

        val hashed = hasher.hash(
            masterPassword + domainName + domainPhrase + versionName
        ).toBase64(url = true).replace("=", "")

        return hashed.take(realLength) + appendSpecial
    }
}