package pro.wsmi.roommap.client.lib.dom

import kotlinx.html.*
import kotlinx.html.stream.createHTML

actual open class HTMLAnchorElement protected constructor (
    override val id: String? = null,
    override val classList: Set<String> = setOf(),
    override val href: String? = null,
    override val hidden: Boolean? = null,
    override val onclick: String? = null
) : HTMLElement(), HTMLHyperlinkElementUtils
{
    override val tagName: String = "a"

    override fun toHTMLString(): String = createHTML().a {
        if (this@HTMLAnchorElement.id != null)
            id = this@HTMLAnchorElement.id!!
        classes = this@HTMLAnchorElement.classList
        if (this@HTMLAnchorElement.href != null)
            href = this@HTMLAnchorElement.href!!
        if (this@HTMLAnchorElement.hidden != null)
            hidden = this@HTMLAnchorElement.hidden!!
        if (this@HTMLAnchorElement.onclick != null)
            onClick = this@HTMLAnchorElement.onclick!!

        unsafe { + this@HTMLAnchorElement.getChildrenHTMLString() }
    }

    actual companion object {
        actual fun create(id: String?, classList: Set<String>, href: String?, hidden: Boolean?, onclick: String?): HTMLAnchorElement = HTMLAnchorElement (
            id = id,
            classList = classList,
            href = href,
            hidden = hidden,
            onclick = onclick
        )
    }
}