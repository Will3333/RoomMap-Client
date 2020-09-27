package pro.wsmi.roommap.client.lib.dom

import kotlinx.html.*
import kotlinx.html.stream.createHTML


actual open class HTMLDivElement protected constructor (
    override val id: String? = null,
    override val classList: Set<String> = setOf(),
    override val hidden: Boolean? = null,
    override val onclick: String? = null
) : HTMLElement()
{
    override val tagName: String = "div"

    override fun toHTMLString(): String = createHTML().div {
        if (this@HTMLDivElement.id != null)
            id = this@HTMLDivElement.id!!
        classes = this@HTMLDivElement.classList
        if (this@HTMLDivElement.hidden != null)
            hidden = this@HTMLDivElement.hidden!!
        if (this@HTMLDivElement.onclick != null)
            onClick = this@HTMLDivElement.onclick!!

        unsafe { + this@HTMLDivElement.getChildrenHTMLString() }
    }

    actual companion object {
        actual fun create(id: String?, classList: Set<String>, hidden: Boolean?, onclick: String?): HTMLDivElement = HTMLDivElement(
            id = id,
            classList = classList,
            hidden = hidden,
            onclick = onclick
        )
    }
}