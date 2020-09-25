package pro.wsmi.roommap.client.matrix_rooms_page

import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement

fun handleMatrixRoomArrowClickEvent(arrowElmId: String)
{
    val nameLabel = document.getElementById(arrowElmId) as HTMLSpanElement?
    val topicLabel = if (nameLabel != null)
        document.getElementById(nameLabel.id.replace("-elm-arrow-", "-topic-elm-")) as HTMLDivElement?
    else null

    if (topicLabel != null)
    {
        if (nameLabel!!.classList.contains("matrix-room-name-down-arrow"))
        {
            nameLabel.classList.remove("matrix-room-name-down-arrow")
            nameLabel.classList.add("matrix-room-name-right-arrow")
            topicLabel.style.display = "none"
        } else {
            nameLabel.classList.remove("matrix-room-name-right-arrow")
            nameLabel.classList.add("matrix-room-name-down-arrow")
            topicLabel.style.display = "block"
        }
    }
}