plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

kotlin {

    jvm()
    js().browser()

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${rootProject.extra["SERIALIZATION_VERSION"]}")
                implementation("pro.wsmi:kwsmilib:${rootProject.extra["KWSMILIB_VERSION"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-html:${rootProject.extra["KHTML_VERSION"]}")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:${rootProject.extra["KTOR_VERSION"]}")
                implementation("io.ktor:ktor-client-js:${rootProject.extra["KTOR_VERSION"]}")
            }
        }
    }
}