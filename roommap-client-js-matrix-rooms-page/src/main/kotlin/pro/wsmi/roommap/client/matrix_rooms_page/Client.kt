package pro.wsmi.roommap.client.matrix_rooms_page

import io.ktor.http.*
import kotlinx.browser.document
import kotlinx.serialization.ExperimentalSerializationApi
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.client.lib.js.getAPIHttpClient
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


external val debugMode: Boolean
external val initialRoomsPerPage: Int
external val initialPageNumber: Int

private val pageReqField = document.getElementById("pageReqField") as HTMLInputElement

var roomsPerPage = initialRoomsPerPage
var pageNumber = initialPageNumber
@ExperimentalSerializationApi
private val apiHttpClient = getAPIHttpClient(USER_AGENT, Url(window.location.protocol))

@ExperimentalSerializationApi
private fun handlePageReqFieldKeydownEvent() = { event: Event ->
    val keyboardEvent = event as KeyboardEvent

    if (keyboardEvent.keyCode == 13) // Enter key
    {
        GlobalScope.launch {
            BusinessData.getAndExecuteOrFail(apiHttpClient) { servers, rooms ->
                println("Old page : $pageNumber")
                pageNumber = pageReqField.value.toInt()
                println("New page : $pageNumber")
            }
        }
    }
}


@ExperimentalSerializationApi
fun main()
{
    pageReqField.addEventListener("keydown", handlePageReqFieldKeydownEvent())
}