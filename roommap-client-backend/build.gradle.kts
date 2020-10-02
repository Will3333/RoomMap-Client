plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
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
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()