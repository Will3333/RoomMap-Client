plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

val SERIALIZATION_VERSION = "1.0.0-RC"
val KWSMILIB_VERSION = "0.6.0"
val KTOR_VERSION = "1.4.0"


kotlin {

    jvm()
    js().browser()

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$SERIALIZATION_VERSION")
                implementation("pro.wsmi:kwsmilib:$KWSMILIB_VERSION")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$KTOR_VERSION")
                implementation("io.ktor:ktor-client-js:$KTOR_VERSION")
            }
        }
    }
}