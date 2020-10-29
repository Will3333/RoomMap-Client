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

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import pro.wsmi.roommap.client.backend.config.ClientConfiguration
import pro.wsmi.roommap.lib.api.PublicAPIMatrixRoom
import pro.wsmi.roommap.lib.api.PublicAPIMatrixRoomListReq
import pro.wsmi.roommap.lib.api.PublicAPIMatrixRoomTag
import pro.wsmi.roommap.lib.api.PublicAPIMatrixServer

@ExperimentalSerializationApi
data class BusinessData (
    val debugMode: Boolean,
    val clientCfg: ClientConfiguration,
    val apiHttpClient: HttpHandler,
    val baseHttpReq: Request,
    var publicAPIData : PublicAPIData = PublicAPIData()
)
{
    private val publicAPIDataMutex: Mutex = Mutex()
    private var publicAPIDataUpdateJob: Job = newPublicAPIDataUpdateJob(CoroutineStart.LAZY)

    private fun newPublicAPIDataUpdateJob(start: CoroutineStart = CoroutineStart.DEFAULT) : Job = GlobalScope.launch(start = start) {

        this@BusinessData.publicAPIDataMutex.withLock {
            this@BusinessData.publicAPIData = getAllPublicAPIData(this@BusinessData.apiHttpClient, this@BusinessData.baseHttpReq).getOrElse { e ->
                if (this@BusinessData.debugMode)
                    e.printStackTrace()
                //TODO add error logger
                this@BusinessData.publicAPIData
            }
        }

        delay(this@BusinessData.clientCfg.publicAPIDataUpdateFrequency)

        this@BusinessData.publicAPIDataUpdateJob = newPublicAPIDataUpdateJob()
    }

    fun startPublicAPIDataUpdateLoop() {
        this.publicAPIDataUpdateJob.start()
    }

    data class PublicAPIData(
        val matrixRoomTags: Map<String, PublicAPIMatrixRoomTag> = mapOf(),
        val matrixServers: Map<String, PublicAPIMatrixServer> = mapOf(),
        val matrixRooms: List<PublicAPIMatrixRoom> = listOf()
    )

    companion object {
        private fun getAllPublicAPIData(httpHandler: HttpHandler, baseHttpReq: Request) : Result<PublicAPIData>
        {
            val matrixRoomTags = requestPublicMatrixRoomTagList(httpHandler, baseHttpReq).getOrElse { e ->
                return Result.failure(e)
            }
            val matrixServers = requestPublicMatrixServerList(httpHandler, baseHttpReq).getOrElse { e ->
                return Result.failure(e)
            }
            val matrixRooms = requestPublicMatrixRoomList(httpHandler, baseHttpReq, PublicAPIMatrixRoomListReq()).getOrElse { e ->
                return Result.failure(e)
            }

            return Result.success(PublicAPIData(
                matrixRoomTags = matrixRoomTags,
                matrixServers = matrixServers,
                matrixRooms = matrixRooms
            ))
        }
    }
}