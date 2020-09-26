package pro.wsmi.roommap.client.lib

import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer
import pro.wsmi.roommap.client.lib.dom.*

const val MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME = "matrix_rooms_per_page"
const val MATRIX_ROOMS_PAGE_PAGE_REQ_NAME = "page"

const val MATRIX_ROOMS_PAGE_DEFAULT_PAGE = 1
const val MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE = 20

val matrixRoomsPageRoomsPerPageStandardNum = listOf(20, 50, 100, 200, 500, 1000)

/*
fun getRoomElementHTML(number: Int, room: MatrixRoom, server: MatrixServer) : List<HTMLElement> = mutableListOf(
    DIV() {
        id = "matrix-room-name-elm-$number"
        classes = setOf("matrix-room-name-elm")
        onClick = "pro.wsmi.roommap.client.matrix_rooms_page.handleMatrixRoomArrowClickEvent(\"$id\")"
        +(room.name ?: room.roomId)
    },
    createHTML().div {
        classes = setOf("matrix-room-nou-elm")
        + room.numJoinedMembers.toString()
    },
    createHTML().div {
        classes = setOf("matrix-room-ga-elm")
        + (if (room.guestCanJoin) "yes" else "no")
    },
    createHTML().div {
        classes = setOf("matrix-room-wr-elm")
        + (if (room.worldReadable) "yes" else "no")
    },
    createHTML().div {
        classes = setOf("matrix-room-server-elm")
        a {
            href = server.apiUrl
            + server.name
        }
    }
).let {
    if (room.topic != null){
        it.add(
            createHTML().div {
                id = "matrix-room-topic-elm-$number"
                classes = setOf("matrix-room-topic-elm")
                + room.topic!!
            }
        )
    }
    it
}.toList()

 */

fun getRoomElementHTML(number: Int, room: MatrixRoom, server: MatrixServer) : DocumentFragment
{
    val docFrag = DocumentFragment()

    val matrixRoomNameArrowSpanId = "matrix-room-elm-arrow-$number"

    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-name-arrow-container"),
            onclick = "handleMatrixRoomArrowClickEvent('$matrixRoomNameArrowSpanId')"
        ).let {
            if (room.topic != null)
            {
                it.appendChild(
                    HTMLSpanElement(
                        id = matrixRoomNameArrowSpanId,
                        classList = setOf("matrix-room-name-arrow", "matrix-room-name-right-arrow")
                    )
                )
            }
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-name-elm")
        ).let {
            it.appendChild(Text(data = room.name ?: room.roomId))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-nou-elm")
        ).let {
            it.appendChild(Text(data = room.numJoinedMembers.toString()))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-ga-elm")
        ).let {
            it.appendChild(Text(data = if (room.guestCanJoin) "yes" else "no"))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-wr-elm")
        ).let {
            it.appendChild(Text(data = if (room.worldReadable) "yes" else "no"))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-server-elm")
        ).let {
            it.appendChild(
                HTMLAnchorElement(
                    href = server.apiUrl
                ).let { apiUrlLink ->
                    apiUrlLink.appendChild(Text(data = server.name))
                    apiUrlLink
                }
            )
            it
        }
    )

    if (room.topic != null) {
        docFrag.appendChild(
            HTMLDivElement (
                id = "matrix-room-topic-elm-$number",
                classList = setOf("matrix-room-topic-elm")
            ).let {
                it.appendChild(Text(data = room.topic))
                it
            }
        )
    }

    return docFrag
}