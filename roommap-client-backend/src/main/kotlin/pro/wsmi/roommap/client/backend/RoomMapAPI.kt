/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.backend

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pro.wsmi.roommap.lib.api.*

@ExperimentalSerializationApi
fun getMatrixServerList(httpHandler: HttpHandler, baseHttpReq: Request) : Result<Map<String, MatrixServer>>
{
    val httpReq = baseHttpReq
        .uri(baseHttpReq.uri.path(APIServerListReq.REQ_PATH))
        .method(Method.GET)

    val httpResponse = httpHandler(httpReq)

    if (httpResponse.status != Status.OK)
        return Result.failure(Exception("The HTTP response code is : ${httpResponse.status.code}"))

    val jsonSerializer = Json {}

    val apiServerListReqResponse = try {
        jsonSerializer.decodeFromString(APIServerListReqResponse.serializer(), httpResponse.bodyString())
    } catch (e: SerializationException) {
        return Result.failure(e)
    }

    if (apiServerListReqResponse.servers.isEmpty())
        return Result.failure(Exception("The Matrix server list received from API is empty."))

    return Result.success(apiServerListReqResponse.servers)
}

@ExperimentalSerializationApi
fun getMatrixRoomList(httpHandler: HttpHandler, baseHttpReq: Request, apiRoomListReq: APIRoomListReq) : Result<List<MatrixRoom>>
{
    val jsonSerializer = Json {}

    val reqBodyStr = try {
        jsonSerializer.encodeToString(APIRoomListReq.serializer(), apiRoomListReq)
    } catch (e: SerializationException) {
        return Result.failure(e)
    }

    val httpReq = baseHttpReq
        .uri(baseHttpReq.uri.path(APIRoomListReq.REQ_PATH))
        .method(Method.POST)
        .body(reqBodyStr)


    val httpResponse = httpHandler(httpReq)

    if (httpResponse.status != Status.OK)
        return Result.failure(Exception("The HTTP response code is : ${httpResponse.status.code}"))


    val apiRoomListReqResponse = try {
        jsonSerializer.decodeFromString(APIRoomListReqResponse.serializer(), httpResponse.bodyString())
    } catch (e: SerializationException) {
        return Result.failure(e)
    }

    if (apiRoomListReqResponse.rooms.isEmpty())
        return Result.failure(Exception("The Matrix room list received from API is empty."))

    return Result.success(apiRoomListReqResponse.rooms)
}