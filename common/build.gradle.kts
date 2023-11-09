plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanager"
version = "1.0.1"

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(17)
    }
    sourceSets {
        val voyagerVersion = extra["voyagerVersion"] as String
        val coroutinesVersion = extra["coroutines.version"] as String
        val loggingVersion = extra["kotlinLoggingVersion"] as String

        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                api(project(":saltthepass"))
                api(project(":storage"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api("io.github.microutils:kotlin-logging-jvm:$loggingVersion")
                api("org.kodein.di:kodein-di:7.20.1")
                api("org.kodein.di:kodein-di-framework-compose:7.20.1")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0")
                api("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-kodein:$voyagerVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("dev.icerock.moko:resources-test:0.23.0")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                api(compose.material3)
                api("cafe.adriel.voyager:voyager-androidx:$voyagerVersion")
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing")
                implementation("org.slf4j:slf4j-simple:2.0.3")
            }
        }
        val desktopTest by getting
    }
}

android {
    namespace = "nl.vanparerensoftwaredevelopment.saltedpassmanager.common"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "nl.vanparerensoftwaredevelopment.saltedpassmanager.resources"
}
