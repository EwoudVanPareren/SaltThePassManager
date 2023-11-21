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
                implementation("org.bouncycastle:bcprov-jdk18on:1.77")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
