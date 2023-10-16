plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanager"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(17)
    }
    sourceSets {
        val coroutinesVersion = extra["coroutines.version"] as String
        val serializationVersion = extra["serializationLibraryVersion"] as String
        val loggingVersion = extra["kotlinLoggingVersion"] as String

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.github.microutils:kotlin-logging-jvm:$loggingVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        val androidMain by getting

        val desktopMain by getting {
            dependencies {
                api("dev.dirs:directories:26")
            }
        }
    }
}

android {
    namespace = "nl.vanparerensoftwaredevelopment.saltedpassmanager.storage"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

