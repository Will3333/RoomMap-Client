package pro.wsmi.roommap.client.jvm

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import freemarker.template.Configuration
import freemarker.template.Version
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pro.wsmi.roommap.client.APP_NAME
import pro.wsmi.roommap.client.APP_VERSION
import pro.wsmi.roommap.client.MatrixRoomPerPage
import pro.wsmi.roommap.client.USER_AGENT
import pro.wsmi.roommap.client.jvm.config.ClientConfiguration
import pro.wsmi.roommap.client.jvm.freemarker.model.MatrixRoom
import pro.wsmi.roommap.client.jvm.freemarker.model.MatrixServer
import pro.wsmi.roommap.client.jvm.freemarker.model.PageInfo
import pro.wsmi.roommap.lib.api.APIRoomListReq
import pro.wsmi.roommap.lib.api.APIRoomListReqResponse
import pro.wsmi.roommap.lib.api.APIServerListReq
import pro.wsmi.roommap.lib.api.APIServerListReqResponse
import java.io.File
import java.io.StringWriter
import kotlin.system.exitProcess


val DEFAULT_CFG_FILE_DIR = File(System.getProperty("user.home"))
const val DEFAULT_CFG_FILE_NAME = ".roommap-client.yml"
const val MAIN_PAGE_TEMPLATE_RELATIVE_PATH = "main_page.ftlh"

@ExperimentalSerializationApi
fun configureServerGlobalHttpFilter(debugMode: Boolean, clientCfg: ClientConfiguration) : Filter
{
    val serverHeaderFilter : Filter = Filter { next: HttpHandler ->
        { req: Request ->
            val originalResponse = next(req)
            originalResponse.header("Server", "$APP_NAME/$APP_VERSION")
        }
    }
    val contentTypeFilter = serverHeaderFilter.then(ServerFilters.SetContentType(ContentType.TEXT_HTML))
    val compressionFilter = if (clientCfg.clientHttpServer.compression) contentTypeFilter.then(ServerFilters.GZip()) else contentTypeFilter
    return if (debugMode) compressionFilter.then(DebuggingFilters.PrintRequestAndResponse()) else compressionFilter
}

@ExperimentalSerializationApi
fun serverRootReqHandler(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerCfg: Configuration, mainPageTemplateFile: File) : (Request) -> Response = { req ->

    val jsonSerializer = Json {
        prettyPrint = debugMode
    }

    val apiHttpClient = ApacheClient()
    val apiHttpReqBase = getAPIHttpRequestBase(USER_AGENT, clientCfg.apiURL)

    val apiServerListReq = apiHttpReqBase
        .uri(apiHttpReqBase.uri.path(APIServerListReq.REQ_PATH))
        .method(Method.GET)
    val apiServerListReqHttpResponse = apiHttpClient(apiServerListReq)

    if (apiServerListReqHttpResponse.status == Status.OK)
    {
        val matrixRoomPerPage = MatrixRoomPerPage.FIFTY

        val apiServerListReqResponse = jsonSerializer.decodeFromString(APIServerListReqResponse.serializer(), apiServerListReqHttpResponse.bodyString())
        val servers = apiServerListReqResponse.servers

        val apiRoomListReq = apiHttpReqBase
            .uri(apiHttpReqBase.uri.path(APIRoomListReq.REQ_PATH))
            .method(Method.POST)
            .body(jsonSerializer.encodeToString(APIRoomListReq.serializer(), APIRoomListReq(end = matrixRoomPerPage.number)))
        val apiRoomListReqHttpResponse = apiHttpClient(apiRoomListReq)

        if (apiRoomListReqHttpResponse.status == Status.OK)
        {
            val apiRoomListReqResponse = jsonSerializer.decodeFromString(APIRoomListReqResponse.serializer(), apiRoomListReqHttpResponse.bodyString())
            val rooms = apiRoomListReqResponse.rooms

            val freemarkerModel = mutableMapOf<String, Any>()

            freemarkerModel["page_info"] = PageInfo(
                1,
                (apiRoomListReqResponse.roomsTotalNum / matrixRoomPerPage.number).let {
                    if ((apiRoomListReqResponse.roomsTotalNum % matrixRoomPerPage.number) != 0) it + 1
                    else it
                }
            )

            freemarkerModel["rooms"] = rooms.map {room ->

                val server = servers[room.serverId]
                if (server != null)
                {
                    MatrixRoom(
                        room.roomId,
                        MatrixServer(server.name, server.apiURL.toString(), server.updateFreq),
                        room.aliases,
                        room.canonicalAlias,
                        room.name,
                        room.numJoinedMembers,
                        room.topic,
                        room.worldReadable,
                        room.guestCanJoin,
                        room.avatarUrl
                    )
                }
                else
                    null
            }

            val mainPageTemplate = freemarkerCfg.getTemplate(mainPageTemplateFile.name)
            val stringWriter = StringWriter()
            mainPageTemplate.process(freemarkerModel, stringWriter)

            Response(Status.OK).body(stringWriter.toString())
        }
        else
        {
            Response(Status.INTERNAL_SERVER_ERROR).body(
                """
            <!DOCTYPE html>
            <html lang="en">
              <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hello World</title>
              </head>
              <body>
                <h1>Status: ${apiRoomListReqHttpResponse.status.code}</h1>
                <p>${apiRoomListReqHttpResponse.status.description}</p>
                <h1>Body</h1>
                <p>${apiRoomListReqHttpResponse.bodyString()}</p>
              </body>
            </html>
            """.trimIndent()
            )
        }
    }
    else
    {
        Response(Status.INTERNAL_SERVER_ERROR).body(
            """
            <!DOCTYPE html>
            <html lang="en">
              <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Hello World</title>
              </head>
              <body>
                <h1>Status: ${apiServerListReqHttpResponse.status.code}</h1>
                <p>${apiServerListReqHttpResponse.status.description}</p>
                <h1>Body</h1>
                <p>${apiServerListReqHttpResponse.bodyString()}</p>
              </body>
            </html>
            """.trimIndent()
        )
    }
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

        println("OK")

        val mainPageTemplateFile = File(resourceDir, MAIN_PAGE_TEMPLATE_RELATIVE_PATH)
        if (!mainPageTemplateFile.exists() || !mainPageTemplateFile.isFile) {
            println("FAILED")
            println("The main page file ${mainPageTemplateFile.canonicalFile} does not exist.")
            exitProcess(10)
        }
        if (!mainPageTemplateFile.canRead()) {
            println("FAILED")
            println("The main page file ${mainPageTemplateFile.canonicalFile} is not readable.")
            exitProcess(11)
        }

        println("Http server starting ... ")

        val freemarkerCfg = Configuration(Version(clientCfg.freeMarkerTemplateVersion))
        freemarkerCfg.setDirectoryForTemplateLoading(resourceDir)
        freemarkerCfg.defaultEncoding = "UTF-8"

        launch {
            configureServerGlobalHttpFilter(debugModeCLA, clientCfg).then(routes(
                "/" bind Method.GET to serverRootReqHandler(debugModeCLA, clientCfg, freemarkerCfg, mainPageTemplateFile)
            )).asServer(Jetty(clientCfg.clientHttpServer.port)).start()
        }

        print("OK")
    }
}

fun main(args: Array<String>) = BaseLineCmd().main(args)