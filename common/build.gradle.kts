plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanagement"
version = "1.0-SNAPSHOT"


kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    sourceSets {
        val voyagerVersion = extra["voyagerVersion"] as String
        val coroutinesVersion = extra["coroutines.version"] as String

        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                api(project(":saltthepass"))
                api(project(":storage"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api("io.github.microutils:kotlin-logging-jvm:3.0.5")
                api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                api("org.kodein.di:kodein-di:7.19.0")
                api("org.kodein.di:kodein-di-framework-compose:7.19.0")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0")

                api("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
//                api("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
//                api("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
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
                api("androidx.core:core-ktx:1.10.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
//                api("cafe.adriel.voyager:voyager-androidx:$voyagerVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing")
            }
        }
        val desktopTest by getting
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "nl.vanparerensoftwaredevelopment.saltedpassmanagement.resources"
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