package pro.wsmi.roommap.client.js.matrix_rooms_page

import pro.wsmi.roommap.client.lib.dom.DocumentFragment
import pro.wsmi.roommap.client.lib.dom.HTMLDivElement
import pro.wsmi.roommap.client.lib.dom.HTMLElement
import pro.wsmi.roommap.client.lib.dom.Text

const val MATRIX_ROOMS_LIST_VIEW_NAME_HEADER_CLASS = "matrix-rooms-name-header"
const val MATRIX_ROOMS_LIST_VIEW_NOU_HEADER_CLASS = "matrix-rooms-nou-header"
const val MATRIX_ROOMS_LIST_VIEW_GA_HEADER_CLASS = "matrix-rooms-ga-header"
const val MATRIX_ROOMS_LIST_VIEW_WR_HEADER_CLASS = "matrix-rooms-wr-header"
const val MATRIX_ROOMS_LIST_VIEW_SERVER_HEADER_CLASS = "matrix-rooms-server-header"

data class MatrixRoomListViewHeader (
    val name: HTMLElement,
    val numberOfUsers: HTMLElement,
    val guestAccess: HTMLElement,
    val worldReadable: HTMLElement,
    val server: HTMLElement
) {
    fun getDocumentFragment() : DocumentFragment {
        val docFrag = DocumentFragment.create()
        docFrag.appendChild(this.name)
        docFrag.appendChild(this.numberOfUsers)
        docFrag.appendChild(this.guestAccess)
        docFrag.appendChild(this.worldReadable)
        docFrag.appendChild(this.server)

        return docFrag
    }

    companion object {
        fun create() : MatrixRoomListViewHeader = MatrixRoomListViewHeader (
            name = HTMLDivElement.create(classList = setOf(MATRIX_ROOMS_LIST_VIEW_NAME_HEADER_CLASS)).let {
                it.appendChild(Text.create("Name"))
                it
            },
            numberOfUsers = HTMLDivElement.create(classList = setOf(MATRIX_ROOMS_LIST_VIEW_NOU_HEADER_CLASS)).let {
                it.appendChild(Text.create("Users"))
                it
            },
            guestAccess = HTMLDivElement.create(classList = setOf(MATRIX_ROOMS_LIST_VIEW_GA_HEADER_CLASS)).let {
                it.appendChild(Text.create("Guest access"))
                it
            },
            worldReadable = HTMLDivElement.create(classList = setOf(MATRIX_ROOMS_LIST_VIEW_WR_HEADER_CLASS)).let {
                it.appendChild(Text.create("World readable"))
                it
            },
            server = HTMLDivElement.create(classList = setOf(MATRIX_ROOMS_LIST_VIEW_SERVER_HEADER_CLASS)).let {
                it.appendChild(Text.create("Server"))
                it
            }
        )
    }
}