package pro.wsmi.roommap.client.js.matrix_rooms_page

import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer
import pro.wsmi.roommap.client.lib.dom.*

data class MatrixRoomListViewRoomElm (
    val name: HTMLElement,
    val numberOfUsers: HTMLElement,
    val guestAccess: HTMLElement,
    val worldReadable: HTMLElement,
    val server: HTMLElement,
    val topic: HTMLElement?
) {

    fun getDocumentFragment() : DocumentFragment {
        val docFrag = DocumentFragment.create()
        docFrag.appendChild(this.name)
        docFrag.appendChild(this.numberOfUsers)
        docFrag.appendChild(this.guestAccess)
        docFrag.appendChild(this.worldReadable)
        docFrag.appendChild(this.server)
        if (this.topic != null)
            docFrag.appendChild(this.topic)

        return docFrag
    }

    companion object {
        fun create(number: Int, room: MatrixRoom, server: MatrixServer) : MatrixRoomListViewRoomElm
        {
            val matrixRoomNameArrowSpanId = "matrix-room-elm-arrow-$number"
            val lineClass = if (number % 2 == 0) "matrix-room-elm-line-var1" else "matrix-room-elm-line-var2"

            return MatrixRoomListViewRoomElm (

                name = HTMLDivElement.create (
                    classList = setOf("matrix-room-name-elm-container", lineClass)
                ).let { mainDiv ->
                    mainDiv.appendChild(
                        if (room.topic != null)
                        {
                            HTMLDivElement.create (
                                classList = setOf("matrix-room-name-arrow-container"),
                                onclick = "handleMatrixRoomArrowClickEvent('$matrixRoomNameArrowSpanId')"
                            ).let {
                                it.appendChild(
                                    HTMLSpanElement.create (
                                        id = matrixRoomNameArrowSpanId,
                                        classList = setOf("matrix-room-name-arrow", "matrix-room-name-right-arrow")
                                    )
                                )
                                it
                            }
                        } else {
                            HTMLDivElement.create (
                                classList = setOf("matrix-room-name-arrow-container"),
                            ).let {
                                it.appendChild(
                                    HTMLSpanElement.create (
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
                            HTMLDivElement.create (
                                classList = setOf("matrix-room-name-elm")
                            ).let {
                                it.appendChild(Text.create(data = room.name ?: room.roomId))
                                it
                            }
                        } else {
                            HTMLDivElement.create (
                                classList = setOf("matrix-room-name-elm-without-arrow")
                            ).let {
                                it.appendChild(Text.create(data = room.name ?: room.roomId))
                                it
                            }
                        }
                    )
                    mainDiv
                },
                numberOfUsers = HTMLDivElement.create (
                    classList = setOf("matrix-room-nou-elm", lineClass)
                ).let {
                    it.appendChild(Text.create(data = room.numJoinedMembers.toString()))
                    it
                },
                guestAccess = HTMLDivElement.create (
                    classList = setOf("matrix-room-ga-elm", lineClass)
                ).let {
                    it.appendChild(Text.create(data = if (room.guestCanJoin) "yes" else "no"))
                    it
                },
                worldReadable = HTMLDivElement.create (
                    classList = setOf("matrix-room-wr-elm", lineClass)
                ).let {
                    it.appendChild(Text.create(data = if (room.worldReadable) "yes" else "no"))
                    it
                },
                server = HTMLDivElement.create (
                    classList = setOf("matrix-room-server-elm", lineClass)
                ).let {
                    it.appendChild(
                        HTMLAnchorElement.create (
                            href = server.apiUrl
                        ).let { apiUrlLink ->
                            apiUrlLink.appendChild(Text.create(data = server.name))
                            apiUrlLink
                        }
                    )
                    it
                },
                if (room.topic != null) {
                    HTMLDivElement.create (
                        id = "matrix-room-topic-elm-$number",
                        classList = setOf("matrix-room-topic-elm")
                    ).let {
                        it.appendChild(Text.create(data = room.topic))
                        it
                    }
                }
                else null
            )
        }
    }
}