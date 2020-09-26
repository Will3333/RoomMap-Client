package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.a
import kotlinx.html.onClick

actual class HTMLAnchorElement actual constructor(
    id: String?,
    classList: Set<String>,
    href: String?,
    onclick: String?
) : HTMLElement(), HTMLHyperlinkElementUtils
{
    override val domEventTarget: org.w3c.dom.HTMLAnchorElement = document.create.a {
        if (id != null)
            this.id = id
        this.classes = classList
        if (href != null)
            this.href = href
        if (onclick != null)
            this.onClick = onclick
    }

    actual override val href: String?
        get() = this.domEventTarget.href
}