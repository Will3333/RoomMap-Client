package pro.wsmi.roommap.client.backend

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import freemarker.template.Configuration
import freemarker.template.Version
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pro.wsmi.roommap.client.backend.config.ClientConfiguration
import pro.wsmi.roommap.client.backend.http4k.APPLICATION_JS
import pro.wsmi.roommap.client.backend.http4k.TEXT_CSS
import pro.wsmi.roommap.client.backend.matrix_rooms_page.handleMatrixRoomsPageReq
import pro.wsmi.roommap.client.lib.APP_NAME
import pro.wsmi.roommap.client.lib.APP_VERSION
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.lib.api.*
import java.io.File
import kotlin.system.exitProcess


val DEFAULT_CFG_FILE_DIR = File(System.getProperty("user.home"))
const val DEFAULT_CFG_FILE_NAME = ".roommap-client.yml"
const val FTLH_FILES_DIR_NAME = "templates"
const val IMG_FILES_DIR_NAME = "img"
const val CSS_FILES_DIR_NAME = "css"
const val JS_FILES_DIR_NAME = "js"
const val MATRIX_ROOMS_PAGE_TEMPLATE_FILE_NAME = "matrix_rooms_page.ftlh"

@ExperimentalSerializationApi
fun configureServerGlobalHttpFilter(debugMode: Boolean, clientCfg: ClientConfiguration) : Filter =
    Filter { next: HttpHandler ->
        { req: Request ->
            val originalResponse = next(req)
            originalResponse.header("Server", "$APP_NAME/$APP_VERSION")
        }
    }.let {
        if (clientCfg.clientHttpServer.compression) it.then(ServerFilters.GZip()) else it
    }.let {
        if (debugMode) it.then(DebuggingFilters.PrintRequestAndResponse()) else it
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
    override fun run() = runBlocking {

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
        val imgDir = File(resourceDir, IMG_FILES_DIR_NAME)
        val cssDir = File(resourceDir, CSS_FILES_DIR_NAME)
        val jsDir = File(resourceDir, JS_FILES_DIR_NAME)

        println("OK")

        print("Checking template of rooms page ... ")

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

        println("OK")


        val matrixServers = mutableMapOf<String, MatrixServer>()
        val matrixRooms = mutableListOf<MatrixRoom>()

        val apiHttpClient = ApacheClient()
        val apiHttpReqBase = getAPIHttpRequestBase(USER_AGENT, clientCfg.apiURL)

        val jsonSerializer = Json {
            prettyPrint = debugModeCLA
        }

        val apiServerListReq = apiHttpReqBase
            .uri(apiHttpReqBase.uri.path(APIServerListReq.REQ_PATH))
            .method(Method.GET)

        val apiRoomListReq = apiHttpReqBase
            .uri(apiHttpReqBase.uri.path(APIRoomListReq.REQ_PATH))
            .method(Method.POST)
            .body(jsonSerializer.encodeToString(APIRoomListReq.serializer(), APIRoomListReq()))

        launch {
            while (true)
            {
                print("Requesting Matrix servers to API ... ")

                val apiServerListReqHttpResponse = apiHttpClient(apiServerListReq)

                if (apiServerListReqHttpResponse.status == Status.OK)
                {
                    val apiServerListReqResponse = jsonSerializer.decodeFromString(APIServerListReqResponse.serializer(), apiServerListReqHttpResponse.bodyString())
                    matrixServers.clear()
                    matrixServers.putAll(apiServerListReqResponse.servers.toSortedMap(compareBy { serverId ->
                        apiServerListReqResponse.servers.getValue(serverId).name
                    }))

                    println("OK")

                    print("Requesting Matrix rooms to API ... ")

                    val apiRoomListReqHttpResponse = apiHttpClient(apiRoomListReq)

                    if (apiRoomListReqHttpResponse.status == Status.OK)
                    {
                        val apiRoomListReqResponse = jsonSerializer.decodeFromString(APIRoomListReqResponse.serializer(), apiRoomListReqHttpResponse.bodyString())
                        matrixRooms.clear()
                        matrixRooms.addAll(apiRoomListReqResponse.rooms)

                        println("OK")
                    }
                    else {
                        println("Failed")
                        println("API HTTP error code: ${apiRoomListReqHttpResponse.status.code}")
                    }
                }
                else {
                    println("Failed")
                    println("API HTTP error code: ${apiServerListReqHttpResponse.status.code}")
                }

                delay(300000)
            }
        }

        print("Http server starting ... ")

        val freemarkerCfg = Configuration(Version(clientCfg.freeMarkerTemplateVersion))
        freemarkerCfg.setDirectoryForTemplateLoading(ftlhDir)
        freemarkerCfg.defaultEncoding = "UTF-8"


        configureServerGlobalHttpFilter(debugModeCLA, clientCfg).then(routes(
            "/static/img" bind static(ResourceLoader.Directory(imgDir.canonicalPath)),
            "/static/css" bind static(ResourceLoader.Directory(cssDir.canonicalPath)),
            "/static/js" bind static(ResourceLoader.Directory(jsDir.canonicalPath)),
            "/{mainLang}/" bind Method.GET to handleMatrixRoomsPageReq(debugModeCLA, clientCfg, freemarkerCfg, matrixRoomsPageTemplateFile, matrixServers, matrixRooms),
            "/" bind Method.GET to handleMatrixRoomsPageReq(debugModeCLA, clientCfg, freemarkerCfg, matrixRoomsPageTemplateFile, matrixServers, matrixRooms)
        )).asServer(Jetty(clientCfg.clientHttpServer.port)).start()


        println("OK")
    }
}

fun main(args: Array<String>) = BaseLineCmd().main(args)