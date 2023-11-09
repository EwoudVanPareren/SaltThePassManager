import org.jetbrains.kotlin.konan.properties.hasProperty
import java.util.Properties

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.reader()?.use { load(it) }
}

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanagement"
version = "1.0.1"

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.window:window:1.1.0")
}

android {
    namespace = "nl.vanparerensoftwaredevelopment.saltedpassmanagement.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "nl.vanparerensoftwaredevelopment.saltedpassmanagement.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }

    // If the project's local.properties contains data on a keystore,
    // use that to sign the release build. Otherwise, leave it unsigned.
    if (localProperties.hasProperty("keystorePath")) {
        signingConfigs {
            create("release") {
                storeFile = rootProject.file(File(localProperties.getProperty("keystorePath")))
                storePassword = localProperties.getProperty("keystorePassword")
                keyAlias = localProperties.getProperty("keystoreReleaseAlias")
                keyPassword = localProperties.getProperty("keystoreReleasePassword")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            if (signingConfigs.names.contains("release")) {
                signingConfig = signingConfigs["release"]
            }
        }
    }
}
