package pro.wsmi.roommap.client.matrix_rooms_page

import kotlinx.browser.document
import kotlinx.serialization.ExperimentalSerializationApi
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.client.lib.js.getAPIHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import pro.wsmi.roommap.lib.api.APIRoomListReq

external val apiUrlStr: String
external val debugMode: Boolean


private val pageReqField = document.getElementById("pageReqField") as HTMLInputElement

private fun handlePageReqFieldKeydownEvent(apiHttpClient: HttpClient) = { event: Event ->
    val keyboardEvent = event as KeyboardEvent

    if (keyboardEvent.keyCode == 13) // Enter key
    {
        val jsonSerializer = Json

        apiHttpClient.post<HttpResponse> {
            url {
                encodedPath = APIRoomListReq.REQ_PATH
            }
            body = TextContent(jsonSerializer.encodeToString(APIRoomListReq.serializer(), APIRoomListReq(start = , end = )), ContentType.Application.Json)
        }
    }
}

@ExperimentalSerializationApi
fun main()
{
    val apiHttpClient = getAPIHttpClient(USER_AGENT, apiUrlStr)

    pageReqField.addEventListener("keydown", handlePageReqFieldKeydownEvent(apiHttpClient))
}