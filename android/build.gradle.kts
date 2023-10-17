plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
//    id("dev.icerock.mobile.multiplatform-resources")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanagement"
version = "1.0"

repositories {
    jcenter()
}

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
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
//        release {
//            isMinifyEnabled = false
//        }
    }
}
