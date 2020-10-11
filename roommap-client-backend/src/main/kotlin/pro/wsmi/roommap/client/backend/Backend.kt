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
import pro.wsmi.roommap.client.lib.APP_NAME
import pro.wsmi.roommap.client.lib.APP_VERSION
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.lib.api.*
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.system.exitProcess


val DEFAULT_CFG_FILE_DIR = File(System.getProperty("user.home"))
const val DEFAULT_CFG_FILE_NAME = ".roommap-client.yml"
const val FTLH_FILES_DIR_NAME = "templates"
const val IMG_FILES_DIR_NAME = "img"
const val CSS_FILES_DIR_NAME = "css"
const val JS_FILES_DIR_NAME = "js"
const val GLOBAL_TEMPLATE_FILE_NAME = "global_template.ftlh"

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

        val globalTemplateFile = File(ftlhDir, GLOBAL_TEMPLATE_FILE_NAME)
        if (!globalTemplateFile.exists() || !globalTemplateFile.isFile) {
            println("FAILED")
            println("The main page file ${globalTemplateFile.canonicalFile} does not exist.")
            exitProcess(10)
        }
        if (!globalTemplateFile.canRead()) {
            println("FAILED")
            println("The main page file ${globalTemplateFile.canonicalFile} is not readable.")
            exitProcess(11)
        }

        println("OK")


        val apiHttpClient = ApacheClient()
        val apiHttpReqBase = getAPIHttpRequestBase(USER_AGENT, clientCfg.apiURL)

        val apiMatrixRoomListReq = APIRoomListReq()

        val businessDataLock = ReentrantLock()
        val businessData = BusinessData()

        launch {
            while (true)
            {
                val firstCall = businessData.matrixServers.isEmpty() && businessData.matrixRooms.isEmpty()


                print("Requesting Matrix servers to API ... ")

                val matrixServerListResult = getMatrixServerList(apiHttpClient, apiHttpReqBase)
                val matrixServerList = matrixServerListResult.getOrElse {
                    println("FAILED")
                    if (this@BaseLineCmd.debugModeCLA) it.printStackTrace()
                    else println(it.localizedMessage)
                    if (firstCall)
                        exitProcess(20)
                    null
                }

                if (matrixServerList != null)
                {
                    println("OK")

                    print("Requesting Matrix rooms to API ... ")

                    val matrixRoomListResult = getMatrixRoomList(apiHttpClient, apiHttpReqBase, apiMatrixRoomListReq)
                    val matrixRoomList = matrixRoomListResult.getOrElse {
                        println("FAILED")
                        if (this@BaseLineCmd.debugModeCLA) it.printStackTrace()
                        else println(it.localizedMessage)
                        if (firstCall)
                            exitProcess(24)
                        null
                    }

                    if (matrixRoomList != null)
                    {
                        println("OK")

                        businessDataLock.lock()
                        businessData.matrixServers = matrixServerList.toSortedMap(compareBy { serverId ->
                            matrixServerList.getValue(serverId).name
                        })
                        businessData.matrixRooms = matrixRoomList
                        businessDataLock.unlock()
                    }
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
            "{path:.*}" bind Method.GET to handleMainHttpRequest(debugModeCLA, clientCfg, freemarkerCfg, globalTemplateFile, businessData, businessDataLock)
        )).asServer(Jetty(clientCfg.clientHttpServer.port)).start()


        println("OK")
    }
}

fun main(args: Array<String>) = BaseLineCmd().main(args)