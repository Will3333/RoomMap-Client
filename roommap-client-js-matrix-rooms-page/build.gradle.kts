/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
        implementation("pro.wsmi:kwsmilib:${rootProject.extra["KWSMILIB_VERSION"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${rootProject.extra["SERIALIZATION_VERSION"]}")
        implementation("io.ktor:ktor-client-core:${rootProject.extra["KTOR_VERSION"]}")
        implementation("io.ktor:ktor-client-js:${rootProject.extra["KTOR_VERSION"]}")
        */
        implementation("org.jetbrains.kotlinx:kotlinx-html-js:${rootProject.extra["KHTML_VERSION"]}")
    }
}