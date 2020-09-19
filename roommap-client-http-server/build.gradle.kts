plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

val ROOMMAP_LIB_VERSION = "0.1.0"
val KWSMILIB_VERSION = "0.6.0"
val SERIALIZATION_VERSION = "1.0.0-RC"
val KAML_VERSION = "0.21.0"
val CLIKT_VERSION = "3.0.0"
val FREEMARKER_VERSION = "2.3.30"
val HTTP4K_VERSION = "3.260.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":roommap-client-lib"))
    implementation("pro.wsmi:roommap-lib:$ROOMMAP_LIB_VERSION")
    implementation("pro.wsmi:kwsmilib:$KWSMILIB_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$SERIALIZATION_VERSION")
    implementation("com.charleskorn.kaml:kaml:$KAML_VERSION")
    implementation("com.github.ajalt.clikt:clikt:$CLIKT_VERSION")
    implementation("org.freemarker:freemarker:$FREEMARKER_VERSION")
    implementation("org.http4k:http4k-core:$HTTP4K_VERSION")
    implementation("org.http4k:http4k-server-jetty:$HTTP4K_VERSION")
    implementation("org.http4k:http4k-client-apache:$HTTP4K_VERSION")
}
