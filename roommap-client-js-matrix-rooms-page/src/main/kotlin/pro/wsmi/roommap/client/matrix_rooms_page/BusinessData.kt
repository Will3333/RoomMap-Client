package pro.wsmi.roommap.client.matrix_rooms_page

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.json.Json
import pro.wsmi.roommap.client.lib.api.*

internal object BusinessData
{
    var fullMatrixServerList: Map<String, MatrixServer>? = null
        private set
    var fullMatrixRoomList: List<MatrixRoom>? = null
        private set

    suspend fun init(httpClient: HttpClient) : BusinessData?
    {
        this.fullMatrixServerList = getFullMatrixServerList(httpClient)
        this.fullMatrixRoomList = getFullMatrixRoomList(httpClient)

        return if (this.isInitialized()) this else null
    }

    fun isInitialized() = this.fullMatrixServerList != null && this.fullMatrixRoomList != null

    suspend fun <T>getAndExecuteOrFail(httpClient: HttpClient, codeToExecute : (fullMatrixServerList: Map<String, MatrixServer>, fullMatrixRoomList: List<MatrixRoom>) -> T) : T?
    {
        val businessData = if (!this.isInitialized()) this.init(httpClient) else this

        return if (businessData != null)
            codeToExecute(businessData.fullMatrixServerList!!, businessData.fullMatrixRoomList!!)
        else {
            println("Unable to get business data")
            null
        }
    }

    private suspend fun getFullMatrixServerList(httpClient: HttpClient) : Map<String, MatrixServer>?
    {
        val jsonSerializer = Json

        val apiHttpResponse = httpClient.get<HttpResponse>{
            url {
                encodedPath = ClientAPIServerListReq.REQ_PATH
            }
        }

        return if (apiHttpResponse.status == HttpStatusCode.OK)
            jsonSerializer.decodeFromString(ClientAPIServerListReqResponse.serializer(), apiHttpResponse.readText(Charsets.UTF_8)).servers
        else null
    }

    private suspend fun getFullMatrixRoomList(httpClient: HttpClient) : List<MatrixRoom>?
    {
        val jsonSerializer = Json

        val apiHttpResponse = httpClient.get<HttpResponse>{
            url {
                encodedPath = ClientAPIRoomListReq.REQ_PATH
            }
        }
        return if (apiHttpResponse.status == HttpStatusCode.OK)
            jsonSerializer.decodeFromString(ClientAPIRoomListReqResponse.serializer(), apiHttpResponse.readText(Charsets.UTF_8)).rooms
        else null
    }
}