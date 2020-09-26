package pro.wsmi.roommap.client.lib.dom

import kotlinx.html.*
import kotlinx.html.stream.createHTML

actual class HTMLSpanElement actual constructor(
    override val id: String?,
    override val classList: Set<String>,
    override val hidden: Boolean?,
    actual override val onclick: String?
) : HTMLElement()
{
    override val tagName: String = "span"

    override fun toHTMLString(): String = createHTML().span {
        if (this@HTMLSpanElement.id != null)
            id = this@HTMLSpanElement.id
        classes = this@HTMLSpanElement.classList
        if (this@HTMLSpanElement.hidden != null)
            hidden = this@HTMLSpanElement.hidden
        if (this@HTMLSpanElement.onclick != null)
            onClick = this@HTMLSpanElement.onclick

        unsafe { + this@HTMLSpanElement.getChildrenHTMLString() }
    }
}