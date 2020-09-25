extra["ROOMMAP_LIB_VERSION"] = "0.1.0"
extra["COROUTINES_VERSION"] = "1.3.9"
extra["SERIALIZATION_VERSION"] = "1.0.0-RC"
extra["KWSMILIB_VERSION"] = "0.8.1"
extra["KHTML_VERSION"]  = "0.7.2"
extra["KTOR_VERSION"] = "1.4.0"

subprojects {

    group = "pro.wsmi"
    version = "0.1.0"

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
    }
}