package pro.wsmi.roommap.client.backend.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.http4k.client.ApacheClient
import org.http4k.core.*
import pro.wsmi.roommap.client.backend.config.ClientConfiguration
import pro.wsmi.roommap.client.backend.getAPIHttpRequestBase
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.client.lib.api.ClientAPIRoomListReqResponse
import pro.wsmi.roommap.client.lib.api.ClientAPIServerListReqResponse
import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer
import pro.wsmi.roommap.lib.api.APIRoomListReq
import pro.wsmi.roommap.lib.api.APIRoomListReqResponse
import pro.wsmi.roommap.lib.api.APIServerListReq
import pro.wsmi.roommap.lib.api.APIServerListReqResponse

@ExperimentalSerializationApi
fun handleMatrixRoomListReq(debugMode: Boolean, clientCfg: ClientConfiguration) : HttpHandler = { req ->
    val jsonSerializer = Json {
        prettyPrint = debugMode
    }

    val apiHttpClient = ApacheClient()
    val apiHttpReqBase = getAPIHttpRequestBase(USER_AGENT, clientCfg.apiURL)

    val apiRoomListReq = apiHttpReqBase
        .uri(apiHttpReqBase.uri.path(APIRoomListReq.REQ_PATH))
        .method(Method.POST)
        .body(jsonSerializer.encodeToString(APIRoomListReq.serializer(), APIRoomListReq()))
    val apiRoomListReqHttpResponse = apiHttpClient(apiRoomListReq)

    when (apiRoomListReqHttpResponse.status)
    {
        Status.OK -> {
            val apiRoomListReqResponse = jsonSerializer.decodeFromString(APIRoomListReqResponse.serializer(), apiRoomListReqHttpResponse.bodyString())
            val clientAPIRoomListReqResponse = apiRoomListReqResponse.rooms.map {
                MatrixRoom(
                    it.roomId,
                    it.serverId,
                    it.aliases,
                    it.canonicalAlias,
                    it.name,
                    it.numJoinedMembers,
                    it.topic,
                    it.worldReadable,
                    it.guestCanJoin,
                    it.avatarUrl
                )
            }.let { ClientAPIRoomListReqResponse(rooms = it) }

            Response(Status.OK)
                .header("Content-Type", ContentType.APPLICATION_JSON.toHeaderValue())
                .body(jsonSerializer.encodeToString(ClientAPIRoomListReqResponse.serializer(), clientAPIRoomListReqResponse))
        }
        Status.NOT_FOUND -> Response(Status.NOT_FOUND)
        else -> Response(Status.INTERNAL_SERVER_ERROR)
    }
}

@ExperimentalSerializationApi
fun handleMatrixServerListReq(debugMode: Boolean, clientCfg: ClientConfiguration) : HttpHandler = { req ->
    val jsonSerializer = Json {
        prettyPrint = debugMode
    }

    val apiHttpClient = ApacheClient()
    val apiHttpReqBase = getAPIHttpRequestBase(USER_AGENT, clientCfg.apiURL)

    val apiServerListReq = apiHttpReqBase
        .uri(apiHttpReqBase.uri.path(APIServerListReq.REQ_PATH))
        .method(Method.GET)
    val apiServerListReqHttpResponse = apiHttpClient(apiServerListReq)

    when (apiServerListReqHttpResponse.status)
    {
        Status.OK -> {
            val apiServerListReqResponse = jsonSerializer.decodeFromString(APIServerListReqResponse.serializer(), apiServerListReqHttpResponse.bodyString())
            val clientAPIServerListReqResponse = apiServerListReqResponse.servers.mapValues {
                MatrixServer(
                    it.value.name,
                    it.value.apiURL.toString(),
                    it.value.updateFreq
                )
            }.let { ClientAPIServerListReqResponse(servers = it) }

            Response(Status.OK)
                .header("Content-Type", ContentType.APPLICATION_JSON.toHeaderValue())
                .body(jsonSerializer.encodeToString(ClientAPIServerListReqResponse.serializer(), clientAPIServerListReqResponse))
        }
        Status.NOT_FOUND -> Response(Status.NOT_FOUND)
        else -> Response(Status.INTERNAL_SERVER_ERROR)
    }
}