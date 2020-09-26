package pro.wsmi.roommap.client.matrix_rooms_page

import io.ktor.http.*
import kotlinx.browser.document
import kotlinx.serialization.ExperimentalSerializationApi
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.client.lib.js.getAPIHttpClient
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement


external val debugMode: Boolean
external val initialRoomsPerPage: Int
external val initialPageNumber: Int

internal val pageReqField = document.getElementById("pageReqField") as HTMLInputElement
internal val matrixRoomsContainer = document.getElementById("matrix-rooms-container") as HTMLDivElement

var roomsPerPage = initialRoomsPerPage
var pageNumber = initialPageNumber
@ExperimentalSerializationApi
internal val apiHttpClient = getAPIHttpClient(USER_AGENT, Url(window.location.protocol))

@ExperimentalSerializationApi
fun main()
{
    pageReqField.addEventListener("keydown", handlePageReqFieldKeydownEvent())
}