plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanager"
version = "1.0"

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.soywiz.korlibs.korio:korio:4.0.2")
                implementation(platform("org.kotlincrypto.hash:bom:0.3.0"))
                implementation("org.kotlincrypto.hash:md")
                implementation("org.kotlincrypto.hash:sha1")
                implementation("org.kotlincrypto.hash:sha2")
                implementation("org.kotlincrypto.hash:sha3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
