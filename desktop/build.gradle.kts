import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "nl.vanparerensoftwaredevelopment.saltedpassmanager"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                implementation("it.sauronsoftware:junique:1.0.4")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SaltedPassManager"
            packageVersion = "1.0.0"
            macOS {
                iconFile.set(project.file("icon-mac.icns"))
            }
            windows {
                iconFile.set(project.file("icon-windows.ico"))
            }
            linux {
                iconFile.set(project.file("icon-linux.png"))
            }
        }
    }
}
