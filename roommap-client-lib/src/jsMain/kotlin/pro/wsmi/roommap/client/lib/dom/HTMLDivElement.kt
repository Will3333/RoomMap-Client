package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.onClick

actual class HTMLDivElement actual constructor(
    id: String?,
    classList: Set<String>,
    onclick: String?
) : HTMLElement() {

    override val domEventTarget : org.w3c.dom.HTMLDivElement = document.create.div {
        if (id != null)
            this.id = id
        classes = classList

        if (onclick != null)
            this.onClick = onclick
    }
}