package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.js.span
import kotlinx.html.onClick

actual open class HTMLSpanElement(domEventTarget: org.w3c.dom.HTMLSpanElement) : HTMLElement(domEventTarget)
{
    actual companion object {
        actual fun create(id: String?, classList: Set<String>, hidden: Boolean?, onclick: String?): HTMLSpanElement = HTMLSpanElement(
            document.create.span {
                if (id != null)
                    this.id = id
                classes = classList
                if (hidden != null)
                    this.hidden = hidden
                if (onclick != null)
                    this.onClick = onclick
            }
        )
    }
}