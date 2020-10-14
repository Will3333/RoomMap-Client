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
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    application
}

val KAML_VERSION = "0.21.0"
val CLIKT_VERSION = "3.0.0"
val FREEMARKER_VERSION = "2.3.30"
val HTTP4K_VERSION = "3.260.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":roommap-client-lib"))
    implementation("pro.wsmi:roommap-lib:${rootProject.extra["ROOMMAP_LIB_VERSION"]}")
    implementation("pro.wsmi:kwsmilib:${rootProject.extra["KWSMILIB_VERSION"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["COROUTINES_VERSION"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${rootProject.extra["SERIALIZATION_VERSION"]}")
    implementation("com.charleskorn.kaml:kaml:$KAML_VERSION")
    implementation("com.github.ajalt.clikt:clikt:$CLIKT_VERSION")
    implementation("org.freemarker:freemarker:$FREEMARKER_VERSION")
    implementation("org.http4k:http4k-core:$HTTP4K_VERSION")
    implementation("org.http4k:http4k-server-jetty:$HTTP4K_VERSION")
    implementation("org.http4k:http4k-client-apache:$HTTP4K_VERSION")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    freeCompilerArgs = freeCompilerArgs.toMutableList().let {
        it.add("-Xallow-result-return-type")
        it
    }.toList()
}

application {
    mainClassName = "pro.wsmi.roommap.client.backend.BackendKt"
}