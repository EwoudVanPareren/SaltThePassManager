package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass

import nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass.ext.toUrlBase64String

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
        require(length > 0) {
            "Length must be 1 or longer"
        }
        val realLength = length - appendSpecial.length
        require(realLength <= hasher.maxLength) {
            "Length ($realLength) too long for hashing function (max is ${hasher.maxLength})"
        }

        val hashed = hasher.hash(
            masterPassword + domainName + domainPhrase + versionName
        ).toUrlBase64String().replace("=", "")

        return hashed.take(realLength) + appendSpecial
    }
}