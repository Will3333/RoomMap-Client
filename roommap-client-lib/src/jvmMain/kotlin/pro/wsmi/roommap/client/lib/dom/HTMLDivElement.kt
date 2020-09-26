package pro.wsmi.roommap.client.lib.dom

import kotlinx.html.*
import kotlinx.html.stream.createHTML

actual class HTMLDivElement actual constructor(
    override val id: String?,
    override val classList: Set<String>,
    actual override val onclick: String?
) : HTMLElement()
{
    override val tagName: String = "div"

    override fun toHTMLString(): String = createHTML().div {
        if (this@HTMLDivElement.id != null)
            id = this@HTMLDivElement.id
        classes = this@HTMLDivElement.classList
        if (this@HTMLDivElement.onclick != null)
            onClick = this@HTMLDivElement.onclick

        unsafe { + this@HTMLDivElement.getChildrenHTMLString() }
    }
}