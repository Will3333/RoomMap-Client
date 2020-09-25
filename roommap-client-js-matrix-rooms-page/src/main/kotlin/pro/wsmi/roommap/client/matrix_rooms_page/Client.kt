package pro.wsmi.roommap.client.matrix_rooms_page

import io.ktor.http.*
import kotlinx.browser.document
import kotlinx.serialization.ExperimentalSerializationApi
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import pro.wsmi.roommap.client.lib.USER_AGENT
import pro.wsmi.roommap.client.lib.js.getAPIHttpClient
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.*
import pro.wsmi.kwsmilib.js.dom.removeAllElementsWithClassNames
import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer


external val debugMode: Boolean
external val initialRoomsPerPage: Int
external val initialPageNumber: Int

private val pageReqField = document.getElementById("pageReqField") as HTMLInputElement
private val matrixRoomsContainer = document.getElementById("matrix-rooms-container") as HTMLDivElement

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

                val newPage = pageReqField.value.toInt()

                val slicedRooms = rooms.slice(IntRange((newPage-1)*roomsPerPage, (newPage*roomsPerPage)-1))

                document.removeAllElementsWithClassNames(listOf(
                    "matrix-room-name-elm",
                    "matrix-room-nou-elm",
                    "matrix-room-ga-elm",
                    "matrix-room-wr-elm",
                    "matrix-room-server-elm",
                    "matrix-room-topic-elm"
                ))

                slicedRooms.forEachIndexed { index, matrixRoom ->
                    val matrixServer = servers[matrixRoom.serverId]
                    if (matrixServer != null) {
                        getRoomElementHTML(index, matrixRoom, matrixServer).forEach {
                            matrixRoomsContainer.append(it)
                        }
                    }
                }

                pageNumber = newPage
            }
        }
    }
}

@ExperimentalSerializationApi
fun main()
{
    pageReqField.addEventListener("keydown", handlePageReqFieldKeydownEvent())

    val matrixRoomNameLabelList = document.getElementsByClassName("matrix-room-name-elm")

    (matrixRoomNameLabelList.asList() as List<HTMLDivElement>).forEach { nameLabel ->
        nameLabel.addEventListener("click", handleMatrixRoomNameClickEvent())
    }
}