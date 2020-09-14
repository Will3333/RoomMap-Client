package pro.wsmi.roommap.client.jvm

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import freemarker.template.Configuration
import freemarker.template.Version
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
import pro.wsmi.roommap.client.jvm.config.ClientConfiguration
import pro.wsmi.roommap.client.jvm.freemarker.model.MatrixRoom
import pro.wsmi.roommap.client.jvm.freemarker.model.MatrixServer
import pro.wsmi.roommap.lib.api.APIRoomListReqResponse
import java.io.File
import java.io.StringWriter
import kotlin.system.exitProcess

const val APP_NAME = "RoomMap-Client"
const val APP_VERSION = "0.1.0"
val DEFAULT_CFG_FILE_DIR = File(System.getProperty("user.home"))
const val DEFAULT_CFG_FILE_NAME = ".roommap-client.yml"
const val MAIN_PAGE_TEMPLATE_FILE_NAME = "main_page.ftlh"

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
fun configureAPIGlobalHttpRequest(method: Method, clientCfg: ClientConfiguration) : Request
{
    val baseReq = Request(
        method,
        Uri(
            scheme = if (clientCfg.apiHttpServer.tls) "https" else "http",
            userInfo = "",
            host = clientCfg.apiHttpServer.server.hostString,
            port = clientCfg.apiHttpServer.server.port,
            path = "",
            query = "",
            fragment = ""
        )
    )

    return baseReq.replaceHeader("User-Agent", "$APP_NAME/$APP_VERSION (${clientCfg.instanceName})")
}

@ExperimentalSerializationApi
fun serverRootReqHandler(clientCfg: ClientConfiguration, freemarkerCfg: Configuration, mainPageTemplateFile: File) : HttpHandler = { req ->

    val apiBaseGetReq = configureAPIGlobalHttpRequest(Method.GET, clientCfg)
    val apiRoomListReq = apiBaseGetReq.uri(apiBaseGetReq.uri.path("/api/rooms"))

    val apiHttpClient = ApacheClient()
    val apiResponse = apiHttpClient(apiRoomListReq)

    if (apiResponse.status == Status.OK)
    {
        val apiRoomListReqResponse = Json.decodeFromString(APIRoomListReqResponse.serializer(), apiResponse.bodyString())

        val freemarkerTemplateModel = mutableMapOf<String, Any>()
        val matrixServerTemplateModel = apiRoomListReqResponse.servers.mapValues {
            MatrixServer(it.value.name, it.value.apiURL.toString(), it.value.updateFreq)
        }
        val matrixRoomListFMModel = mutableListOf<MatrixRoom>()
        apiRoomListReqResponse.rooms.forEach{ (serverId, rooms) ->
            rooms.forEach { room ->
                matrixRoomListFMModel.add(
                    MatrixRoom(
                        room.roomId,
                        matrixServerTemplateModel[serverId]!!,
                        room.aliases,
                        room.canonicalAlias,
                        room.name,
                        room.numJoinedMembers,
                        room.topic,
                        room.worldReadable,
                        room.guestCanJoin,
                        room.avatarUrl
                    )
                )
            }
        }
        matrixRoomListFMModel.sortByDescending {
            it.num_joined_members
        }
        freemarkerTemplateModel["rooms"] = matrixRoomListFMModel

        val mainPageTemplate = freemarkerCfg.getTemplate(mainPageTemplateFile.name)
        val stringWriter = StringWriter()
        mainPageTemplate.process(freemarkerTemplateModel, stringWriter)

        Response(Status.OK).body(stringWriter.toString())
    }
    else
    {
        Response(Status.OK).body("""<!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Hello World</title>
          </head>
          <body>
            <h1>Status: ${apiResponse.status.code}</h1>
            <p>${apiResponse.status.description}</p>
            <h1>Body</h1>
            <p>${apiResponse.bodyString()}</p>
          </body>
        </html>""".trimIndent())
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
    override fun run()
    {
        print("Loading of client configuration ... ")

        val configFile = this.cfgFilePathCLA ?: File(DEFAULT_CFG_FILE_DIR, DEFAULT_CFG_FILE_NAME)
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

        val mainPageTemplateFile = File(DEFAULT_CFG_FILE_DIR, MAIN_PAGE_TEMPLATE_FILE_NAME)
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

        val clientCfg = try {
            Yaml.default.decodeFromString(ClientConfiguration.serializer(), configFile.readText(Charsets.UTF_8))
        } catch (e: Exception) {
            println("FAILED")
            println("There is an error in the configuration file ${configFile.canonicalFile}.")
            if (this@BaseLineCmd.debugModeCLA) e.printStackTrace()
            else println(e.localizedMessage)
            exitProcess(3)
        }

        println("OK")

        println("Http server starting ... ")

        val freemarkerCfg = Configuration(Version(clientCfg.freeMarkerTemplateVersion))
        freemarkerCfg.setDirectoryForTemplateLoading(DEFAULT_CFG_FILE_DIR)
        freemarkerCfg.defaultEncoding = "UTF-8"

        configureServerGlobalHttpFilter(debugModeCLA, clientCfg).then(routes(
            "/" bind Method.GET to serverRootReqHandler(clientCfg, freemarkerCfg, mainPageTemplateFile)
        )).asServer(Jetty(clientCfg.clientHttpServer.port)).start()

        print("OK")
    }
}

fun main(args: Array<String>) = BaseLineCmd().main(args)