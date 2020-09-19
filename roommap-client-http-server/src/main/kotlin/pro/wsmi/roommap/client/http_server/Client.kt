package pro.wsmi.roommap.client.http_server

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import freemarker.template.Configuration
import freemarker.template.Version
import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pro.wsmi.roommap.client.http_server.config.ClientConfiguration
import pro.wsmi.roommap.client.http_server.http4k.APPLICATION_JS
import pro.wsmi.roommap.client.http_server.http4k.TEXT_CSS
import pro.wsmi.roommap.client.lib.APP_NAME
import pro.wsmi.roommap.client.lib.APP_VERSION
import java.io.File
import kotlin.system.exitProcess


val DEFAULT_CFG_FILE_DIR = File(System.getProperty("user.home"))
const val DEFAULT_CFG_FILE_NAME = ".roommap-client.yml"
const val FTLH_FILES_DIR_NAME = "templates"
const val CSS_FILES_DIR_NAME = "css"
const val JS_FILES_DIR_NAME = "js"
const val MATRIX_ROOMS_PAGE_TEMPLATE_FILE_NAME = "matrix_rooms_page.ftlh"

@ExperimentalSerializationApi
fun configureServerGlobalHttpFilter(debugMode: Boolean, clientCfg: ClientConfiguration) : Filter
{
    val serverHeaderFilter : Filter = Filter { next: HttpHandler ->
        { req: Request ->
            val originalResponse = next(req)
            originalResponse.header("Server", "$APP_NAME/$APP_VERSION")
        }
    }
    val compressionFilter = if (clientCfg.clientHttpServer.compression) serverHeaderFilter.then(ServerFilters.GZip()) else serverHeaderFilter
    return if (debugMode) compressionFilter.then(DebuggingFilters.PrintRequestAndResponse()) else compressionFilter
}

class BaseLineCmd : CliktCommand(name = "RoomMapClient")
{
    private val cfgFilePathCLA: File? by option("-f", "--config-file", help = "Path of the client configuration file")
        .file (
            mustExist = true,
            canBeFile = true,
            canBeDir = false,
            mustBeReadable = true,
            mustBeWritable = false,
            canBeSymlink = true
        )
    private val debugModeCLA by option("--debug", help = "Turn on the debug mode").flag()

    @ExperimentalSerializationApi
    override fun run() {

        print("Loading of client configuration ... ")

        val configFile = this@BaseLineCmd.cfgFilePathCLA ?: File(DEFAULT_CFG_FILE_DIR, DEFAULT_CFG_FILE_NAME)
        if (!configFile.exists() || !configFile.isFile) {
            println("FAILED")
            println("The configuration file ${configFile.canonicalFile} does not exist.")
            exitProcess(1)
        }
        if (!configFile.canRead()) {
            println("FAILED")
            println("The configuration file ${configFile.canonicalFile} is not readable.")
            exitProcess(2)
        }

        val clientCfg = try {
            Yaml.default.decodeFromString(ClientConfiguration.serializer(), configFile.readText(Charsets.UTF_8))
        } catch (e: Exception) {
            println("FAILED")
            println("There is an error in the configuration file ${configFile.canonicalFile}.")
            if (this@BaseLineCmd.debugModeCLA) e.printStackTrace()
            else println(e.localizedMessage)
            exitProcess(3)
        }

        val resourceDir = clientCfg.resourceDirectory
        if (!resourceDir.exists() || !resourceDir.isDirectory)
        {
            println("FAILED")
            println("The resources directory ${resourceDir.canonicalFile} does not exist.")
            exitProcess(4)
        }
        if (!resourceDir.canRead()) {
            println("FAILED")
            println("The resources directory ${resourceDir.canonicalFile} is not readable.")
            exitProcess(5)
        }

        val ftlhDir = File(resourceDir, FTLH_FILES_DIR_NAME)
        val cssDir = File(resourceDir, CSS_FILES_DIR_NAME)
        val jsDir = File(resourceDir, JS_FILES_DIR_NAME)

        println("OK")

        val matrixRoomsPageTemplateFile = File(ftlhDir, MATRIX_ROOMS_PAGE_TEMPLATE_FILE_NAME)
        if (!matrixRoomsPageTemplateFile.exists() || !matrixRoomsPageTemplateFile.isFile) {
            println("FAILED")
            println("The main page file ${matrixRoomsPageTemplateFile.canonicalFile} does not exist.")
            exitProcess(10)
        }
        if (!matrixRoomsPageTemplateFile.canRead()) {
            println("FAILED")
            println("The main page file ${matrixRoomsPageTemplateFile.canonicalFile} is not readable.")
            exitProcess(11)
        }

        print("Http server starting ... ")

        val freemarkerCfg = Configuration(Version(clientCfg.freeMarkerTemplateVersion))
        freemarkerCfg.setDirectoryForTemplateLoading(ftlhDir)
        freemarkerCfg.defaultEncoding = "UTF-8"


        configureServerGlobalHttpFilter(debugModeCLA, clientCfg).then(routes(
            "/static/css" bind static(ResourceLoader.Directory(cssDir.canonicalPath), Pair("css", ContentType.TEXT_CSS)),
            "/static/js" bind static(ResourceLoader.Directory(jsDir.canonicalPath), Pair("js", ContentType.APPLICATION_JS), Pair("js.map", ContentType.TEXT_PLAIN)),
            "/" bind Method.GET to handleMatrixRoomsPageReq(debugModeCLA, clientCfg, freemarkerCfg, matrixRoomsPageTemplateFile)
        )).asServer(Jetty(clientCfg.clientHttpServer.port)).start()


        println("OK")
    }
}

fun main(args: Array<String>) = BaseLineCmd().main(args)