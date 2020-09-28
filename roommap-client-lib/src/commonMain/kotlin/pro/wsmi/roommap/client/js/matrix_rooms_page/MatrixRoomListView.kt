package pro.wsmi.roommap.client.js.matrix_rooms_page

import pro.wsmi.roommap.client.lib.api.MatrixRoom
import pro.wsmi.roommap.client.lib.api.MatrixServer
import pro.wsmi.roommap.client.lib.dom.DocumentFragment

data class MatrixRoomListView (val header: MatrixRoomListViewHeader, val matrixRoomElmList: MutableList<MatrixRoomListViewRoomElm>)
{
    fun getDocumentFragment() : DocumentFragment {
        val docFrag = DocumentFragment.create()

        docFrag.appendChild(this.header.getDocumentFragment())
        this.matrixRoomElmList.forEach {
            docFrag.appendChild(it.getDocumentFragment())
        }

        return docFrag
    }

    companion object {
        fun create(servers: Map<String, MatrixServer>, rooms: List<MatrixRoom>) : MatrixRoomListView = MatrixRoomListView (
            MatrixRoomListViewHeader.create(),
            rooms.mapIndexedNotNull { index, room ->

                val server = servers[room.serverId]
                if (server != null)
                    MatrixRoomListViewRoomElm.create(index, room, server)
                else null

            }.toMutableList()
        )
    }
}