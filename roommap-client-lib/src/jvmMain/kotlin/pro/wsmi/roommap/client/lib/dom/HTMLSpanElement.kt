package pro.wsmi.roommap.client.lib.dom

import kotlinx.html.*
import kotlinx.html.stream.createHTML

actual open class HTMLSpanElement protected constructor (
    override val id: String?,
    override val classList: Set<String>,
    override val hidden: Boolean?,
    override val onclick: String?
) : HTMLElement()
{
    override val tagName: String = "span"

    override fun toHTMLString(): String = createHTML().span {
        if (this@HTMLSpanElement.id != null)
            id = this@HTMLSpanElement.id!!
        classes = this@HTMLSpanElement.classList
        if (this@HTMLSpanElement.hidden != null)
            hidden = this@HTMLSpanElement.hidden!!
        if (this@HTMLSpanElement.onclick != null)
            onClick = this@HTMLSpanElement.onclick!!

        unsafe { + this@HTMLSpanElement.getChildrenHTMLString() }
    }

    actual companion object {
        actual fun create(id: String?, classList: Set<String>, hidden: Boolean?, onclick: String?): HTMLSpanElement = HTMLSpanElement (
            id = id,
            classList = classList,
            hidden = hidden,
            onclick = onclick
        )
    }
}