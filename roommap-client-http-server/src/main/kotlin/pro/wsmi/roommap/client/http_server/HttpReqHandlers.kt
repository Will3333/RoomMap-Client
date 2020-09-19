package pro.wsmi.roommap.client.http_server

import freemarker.template.Configuration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.lens.Query
import org.http4k.lens.int
import pro.wsmi.roommap.client.http_server.config.ClientConfiguration
import pro.wsmi.roommap.client.MatrixRoom
import pro.wsmi.roommap.client.lib.*
import pro.wsmi.roommap.lib.api.APIRoomListReq
import pro.wsmi.roommap.lib.api.APIRoomListReqResponse
import pro.wsmi.roommap.lib.api.APIServerListReq
import pro.wsmi.roommap.lib.api.APIServerListReqResponse
import java.io.File
import java.io.StringWriter

private val rootReqPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_PAGE_REQ_NAME)
private val rootReqElmPerPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME)

@ExperimentalSerializationApi
fun handleMatrixRoomsPageReq(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerCfg: Configuration, mainPageTemplateFile: File) : (Request) -> Response = { req ->

    val pageReq = rootReqPageQuery(req).let {
        if (it != null && it > 0) it
        else MATRIX_ROOMS_PAGE_DEFAULT_PAGE
    }
    val elmPerPage = rootReqElmPerPageQuery(req).let {
        if (it != null && it > 0) it
        else MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE
    }

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
        val apiServerListReqResponse = jsonSerializer.decodeFromString(APIServerListReqResponse.serializer(), apiServerListReqHttpResponse.bodyString())
        val servers = apiServerListReqResponse.servers

        val apiRoomListReq = apiHttpReqBase
            .uri(apiHttpReqBase.uri.path(APIRoomListReq.REQ_PATH))
            .method(Method.POST)
            .body(jsonSerializer.encodeToString(APIRoomListReq.serializer(), APIRoomListReq(start = elmPerPage*(pageReq-1), end = elmPerPage*pageReq-1)))
        val apiRoomListReqHttpResponse = apiHttpClient(apiRoomListReq)

        if (apiRoomListReqHttpResponse.status == Status.OK)
        {
            val apiRoomListReqResponse = jsonSerializer.decodeFromString(APIRoomListReqResponse.serializer(), apiRoomListReqHttpResponse.bodyString())
            val rooms = apiRoomListReqResponse.rooms


            val freemarkerModel = mapOf(
                "debug_mode" to debugMode,
                "api_url" to clientCfg.apiURL.toString(),
                "queries" to mapOf(
                    "rooms_per_page_req_name" to MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME,
                    "page_req_name" to MATRIX_ROOMS_PAGE_PAGE_REQ_NAME
                ),

                "page_info" to MatrixRoomsPageInfo(
                    pageReq,
                    (apiRoomListReqResponse.roomsTotalNum / elmPerPage).let {
                        if ((apiRoomListReqResponse.roomsTotalNum % elmPerPage) != 0) it + 1
                        else it
                    },
                    elmPerPage,
                    matrixRoomsPageRoomsPerPageStandardNum
                ),

                "rooms" to rooms.map {room ->

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
            )

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
    }.header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
}