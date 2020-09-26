package pro.wsmi.roommap.client.matrix_rooms_page

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import pro.wsmi.kwsmilib.js.dom.removeAllElementsWithClassNames
import pro.wsmi.roommap.client.lib.dom.appendChild
import pro.wsmi.roommap.client.lib.getRoomElementHTML


@ExperimentalSerializationApi
internal fun handlePageReqFieldKeydownEvent() = { event: Event ->
    val keyboardEvent = event as KeyboardEvent

    if (keyboardEvent.keyCode == 13) // Enter key
    {
        GlobalScope.launch {
            BusinessData.getAndExecuteOrFail(apiHttpClient) { servers, rooms ->

                val newPage = pageReqField.value.toInt()

                val slicedRooms = rooms.slice(IntRange((newPage-1)*roomsPerPage, (newPage*roomsPerPage)-1))

                document.removeAllElementsWithClassNames(listOf(
                    "matrix-room-name-elm-container",
                    "matrix-room-nou-elm",
                    "matrix-room-ga-elm",
                    "matrix-room-wr-elm",
                    "matrix-room-server-elm",
                    "matrix-room-topic-elm"
                ))

                slicedRooms.forEachIndexed { index, matrixRoom ->
                    val matrixServer = servers[matrixRoom.serverId]
                    if (matrixServer != null) {
                        matrixRoomsContainer.appendChild(getRoomElementHTML(index, matrixRoom, matrixServer))
                    }
                }

                pageNumber = newPage
            }
        }
    }
}
