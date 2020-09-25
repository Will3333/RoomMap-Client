package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.id
import kotlinx.html.js.div
import kotlinx.html.onClick
import org.w3c.dom.asList

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

    actual override val id: String?
        get() = this.domEventTarget.id
    actual override val classList: Set<String>
        get() = this.domEventTarget.classList.asList().toSet()
    actual override val onclick: String?
        get() = this.domEventTarget.onclick.toString()
    actual override val tagName: String
        get() = this.domEventTarget.tagName
}