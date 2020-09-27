package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.js.a
import kotlinx.html.onClick

actual open class HTMLAnchorElement(domEventTarget: org.w3c.dom.HTMLAnchorElement) : HTMLElement(domEventTarget), HTMLHyperlinkElementUtils
{
    override val href: String?
        get() = (this.domEventTarget as org.w3c.dom.HTMLAnchorElement).href

    actual companion object {
        actual fun create(id: String?, classList: Set<String>, href: String?, hidden: Boolean?, onclick: String?): HTMLAnchorElement = HTMLAnchorElement(
            document.create.a {
                if (id != null)
                    this.id = id
                this.classes = classList
                if (href != null)
                    this.href = href
                if (hidden != null)
                    this.hidden = hidden
                if (onclick != null)
                    this.onClick = onclick
            }
        )
    }
}