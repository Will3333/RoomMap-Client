package pro.wsmi.roommap.client.lib

import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer
import pro.wsmi.roommap.client.lib.dom.*

const val MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME = "matrix_rooms_per_page"
const val MATRIX_ROOMS_PAGE_PAGE_REQ_NAME = "page"

const val MATRIX_ROOMS_PAGE_DEFAULT_PAGE = 1
const val MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE = 20


fun getRoomElementHTML(number: Int, room: MatrixRoom, server: MatrixServer) : DocumentFragment
{
    val docFrag = DocumentFragment()

    val matrixRoomNameArrowSpanId = "matrix-room-elm-arrow-$number"
    val lineClass = if (number % 2 == 0) "matrix-room-elm-line-var1" else "matrix-room-elm-line-var2"

    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-name-elm-container", lineClass)
        ).let { mainDiv ->
            mainDiv.appendChild(
                if (room.topic != null)
                {
                    HTMLDivElement (
                        classList = setOf("matrix-room-name-arrow-container"),
                        onclick = "handleMatrixRoomArrowClickEvent('$matrixRoomNameArrowSpanId')"
                    ).let {
                        it.appendChild(
                            HTMLSpanElement(
                                id = matrixRoomNameArrowSpanId,
                                classList = setOf("matrix-room-name-arrow", "matrix-room-name-right-arrow")
                            )
                        )
                        it
                    }
                } else {
                    HTMLDivElement (
                        classList = setOf("matrix-room-name-arrow-container"),
                    ).let {
                        it.appendChild(
                            HTMLSpanElement(
                                id = matrixRoomNameArrowSpanId,
                                hidden = true
                            )
                        )
                        it
                    }
                }
            )
            mainDiv.appendChild(
                if (room.topic != null)
                {
                    HTMLDivElement (
                        classList = setOf("matrix-room-name-elm")
                    ).let {
                        it.appendChild(Text(data = room.name ?: room.roomId))
                        it
                    }
                } else {
                    HTMLDivElement (
                        classList = setOf("matrix-room-name-elm-without-arrow")
                    ).let {
                        it.appendChild(Text(data = room.name ?: room.roomId))
                        it
                    }
                }
            )
            mainDiv
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-nou-elm", lineClass)
        ).let {
            it.appendChild(Text(data = room.numJoinedMembers.toString()))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-ga-elm", lineClass)
        ).let {
            it.appendChild(Text(data = if (room.guestCanJoin) "yes" else "no"))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-wr-elm", lineClass)
        ).let {
            it.appendChild(Text(data = if (room.worldReadable) "yes" else "no"))
            it
        }
    )
    docFrag.appendChild(
        HTMLDivElement (
            classList = setOf("matrix-room-server-elm", lineClass)
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