package nl.vanparerensoftwaredevelopment.saltthepassmanager.saltthepass

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

internal class SaltThePassTest {
    /**
     * Test whether the algorithm matches the
     * original [SaltThePass.com](https://saltthepass.com)
     * by comparing with results from there.
     */
    @Test
    fun testMatchOriginalSaltThePass() {
        // There's a test for each hashing function

        assertEquals(
            expected = "tbPsCpHRRyKVsxi",
            actual = SaltThePass.hash(
                masterPassword = "It's a test today",
                domainName = "example.com",
                domainPhrase = "whatisit",
                length = 15,
                hasher = Hashers.md5
            ),
            message = "Generated password does not match with SaltThePass.com on MD-5"
        )

        assertEquals(
            expected = "onM3v_nA7BwFj3he-nDZ",
            actual = SaltThePass.hash(
                // Referencing https://xkcd.com/936/
                masterPassword = "correct horse battery staple",
                domainName = "wikipedia.com",
                domainPhrase = "example@example.com",
                length = 20,
                hasher = Hashers.sha1
            ),
            message = "Generated password does not match with SaltThePass.com on SHA-1"
        )

        assertEquals(
            expected = "UhiQ5ciCKZ1J7GywfGHz",
            actual = SaltThePass.hash(
                masterPassword = "Lorem ipsum dolor sit amet",
                domainName = "github.com",
                domainPhrase = "WorkAccountUserName",
                length = 20,
                hasher = Hashers.sha2
            ),
            message = "Generated password does not match with SaltThePass.com on SHA-2"
        )

        assertEquals(
            expected = "I4CVJEONsHU4mpz4D20T0mTP-LDDGaz4uw2eHEYx",
            actual = SaltThePass.hash(
                masterPassword = "Ut enim ad minim veniam",
                domainName = "account.jetbrains.com",
                domainPhrase = "example@example.com",
                length = 40,
                /*
                 * Due to some historical confusion, what is listed
                 * as "SHA-3" on SaltThePass.com is actually Keccak512,
                 * so we're using Keccak512 here.
                 */
                hasher = Hashers.keccak512
            ),
            message = "Generated password does not match with SaltThePass.com on Keccak512 (\"SHA-3\" on SaltThePass.com)"
        )

        // TODO: Add RIPEMD-160 test once that hash function is supported
    }


    /**
     * A basic SHA-3 test.
     */
    @Test
    fun testSha3() {
        assertEquals(
            expected = "0vfHxdfgw0AgrdFSrYxj",
            actual = SaltThePass.hash(
                masterPassword = "It's a test today",
                domainName = "example.com",
                domainPhrase = "whatisit",
                length = 20,
                hasher = Hashers.sha3
            ),
            message = "Generated password is incorrect on SHA-3"
        )
    }

    /**
     * Test the length parameter
     */
    @Test
    fun testLength() {
        // Test it for each hashing function
        Hashers.all.forEach { hasher ->
            // Try all available lengths
            (1..hasher.maxLength).forEach { length ->
                val generated = SaltThePass.hash(
                    masterPassword = "Example",
                    domainName = "example.com",
                    domainPhrase = "",
                    length = length,
                    hasher = hasher
                )

                assertEquals(
                    expected = length,
                    actual = generated.length,
                    message = "Generated password length is incorrect"
                )
            }
        }

        assertFailsWith<IllegalArgumentException>(
            message = "Did not fail on too-high length"
        ) {
            SaltThePass.hash(
                masterPassword = "Example",
                domainName = "example.com",
                domainPhrase = "",
                length = Hashers.default.maxLength + 1,
                hasher = Hashers.default
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "Did not fail on zero length"
        ) {
            SaltThePass.hash(
                masterPassword = "Example",
                domainName = "example.com",
                domainPhrase = "",
                length = 0,
                hasher = Hashers.default
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "Did not fail on negative length"
        ) {
            SaltThePass.hash(
                masterPassword = "Example",
                domainName = "example.com",
                domainPhrase = "",
                length = -5,
                hasher = Hashers.default
            )
        }
    }

    /**
     * Test whether the version name works correctly.
     */
    @Test
    fun testVersionName() {
        val withVersionName = SaltThePass.hash(
            masterPassword = "It's a test today",
            domainName = "example.com",
            domainPhrase = "whatisit",
            versionName = "2",
            length = 20,
            hasher = Hashers.default
        )

        // It has to be different from not using a version name
        assertNotEquals(
            illegal = withVersionName,
            actual = SaltThePass.hash(
                masterPassword = "It's a test today",
                domainName = "example.com",
                domainPhrase = "whatisit",
                length = 20,
                hasher = Hashers.default
            ),
            message = "Version name has no effect"
        )

        /*
         * It should be the same as if the version name was appended
         * to domain phrase
         */
        assertEquals(
            expected = withVersionName,
            actual = SaltThePass.hash(
                masterPassword = "It's a test today",
                domainName = "example.com",
                domainPhrase = "whatisit2",
                length = 20,
                hasher = Hashers.default
            ),
            message = "Version name isn't applied correctly"
        )
    }

    /**
     * Test whether the append-special parameter works correctly.
     */
    @Test
    fun testAppendSpecial() {
        val withoutAppendSpecial = SaltThePass.hash(
            masterPassword = "It's a test today",
            domainName = "example.com",
            domainPhrase = "whatisit",
            length = 20,
            hasher = Hashers.default
        )

        assertEquals(
            expected = withoutAppendSpecial.dropLast(2) + "@@",
            actual = SaltThePass.hash(
                masterPassword = "It's a test today",
                domainName = "example.com",
                domainPhrase = "whatisit",
                appendSpecial = "@@",
                length = 20,
                hasher = Hashers.default
            ),
            message = "Append special isn't applied correctly"
        )
    }
}