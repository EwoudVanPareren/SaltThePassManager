package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass

/**
 * Generic interface for hashing functions.
 */
interface Hasher {
    val name: String
    val maxLength: Int
    fun hash(input: String): ByteArray

    companion object {
        internal operator fun invoke(name: String, maxLength: Int, hasher: (String) -> ByteArray): Hasher {
            return object: Hasher {
                override val name = name
                override val maxLength = maxLength
                override fun hash(input: String) = hasher(input)
            }
        }
    }
}