buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.23.0")
    }
}

group = "nl.vanparerensoftwaredevelopment.saltthepassmanager"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        // Used for jUnique
        maven("https://github.com/poolborges/maven/raw/master/thirdparty/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}