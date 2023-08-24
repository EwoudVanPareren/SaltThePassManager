plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanagement"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.1")
    implementation("androidx.window:window:1.0.0")
}

android {
    compileSdkVersion(33)
    defaultConfig {
        applicationId = "nl.vanparerensoftwaredevelopment.saltedpassmanagement.android"
        minSdkVersion(24)
        targetSdkVersion(33)
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}