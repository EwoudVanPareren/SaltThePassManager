plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanagement"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    sourceSets {
        val coroutinesVersion = extra["coroutines.version"] as String
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
                api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                api("io.github.xxfast:kstore:0.6.0")
                api("io.github.xxfast:kstore-file:0.6.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.startup:startup-runtime:1.1.1")
            }
        }

        val desktopMain by getting {
            dependencies {
                api("dev.dirs:directories:26")
            }
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}