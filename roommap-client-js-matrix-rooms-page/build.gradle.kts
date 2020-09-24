plugins {
    id("org.jetbrains.kotlin.js") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

val ROOMMAP_LIB_VERSION = "0.1.0"
val COROUTINES_VERSION = "1.3.9"
val KWSMILIB_VERSION = "0.8.0"
val SERIALIZATION_VERSION = "1.0.0-RC"
val KTOR_VERSION = "1.4.0"
val KHTML_VERSION = "0.7.2"

kotlin {
    js {
        browser {
            webpackTask {
                cssSupport.enabled = true
            }

            runTask {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }

    dependencies {
        implementation(kotlin("stdlib-js"))
        implementation(project(":roommap-client-lib"))
        implementation("pro.wsmi:roommap-lib:$ROOMMAP_LIB_VERSION")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$COROUTINES_VERSION")
        implementation("pro.wsmi:kwsmilib:$KWSMILIB_VERSION")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$SERIALIZATION_VERSION")
        implementation("io.ktor:ktor-client-core:$KTOR_VERSION")
        implementation("io.ktor:ktor-client-js:$KTOR_VERSION")
        implementation("org.jetbrains.kotlinx:kotlinx-html-js:$KHTML_VERSION")
    }
}