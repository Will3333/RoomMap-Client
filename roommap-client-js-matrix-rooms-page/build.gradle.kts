plugins {
    id("org.jetbrains.kotlin.js") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

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
        /*
        implementation("pro.wsmi:roommap-lib:${rootProject.extra["ROOMMAP_LIB_VERSION"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["COROUTINES_VERSION"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${rootProject.extra["COROUTINES_VERSION"]}")
        implementation("pro.wsmi:kwsmilib:${rootProject.extra["KWSMILIB_VERSION"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${rootProject.extra["SERIALIZATION_VERSION"]}")
        implementation("io.ktor:ktor-client-core:${rootProject.extra["KTOR_VERSION"]}")
        implementation("io.ktor:ktor-client-js:${rootProject.extra["KTOR_VERSION"]}")
        */
        implementation("org.jetbrains.kotlinx:kotlinx-html-js:${rootProject.extra["KHTML_VERSION"]}")
    }
}