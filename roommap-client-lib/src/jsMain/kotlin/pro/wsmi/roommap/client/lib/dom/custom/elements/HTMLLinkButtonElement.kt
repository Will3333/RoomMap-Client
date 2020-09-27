package pro.wsmi.roommap.client.lib.dom.custom.elements

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.dom.create
import kotlinx.html.hidden
import kotlinx.html.id
import kotlinx.html.js.span
import kotlinx.html.onClick
import pro.wsmi.roommap.client.lib.dom.HTMLAnchorElement
import pro.wsmi.roommap.client.lib.dom.HTMLHyperlinkElementUtils
import pro.wsmi.roommap.client.lib.dom.HTMLSpanElement

actual open class HTMLLinkButtonElement protected constructor(protected var anchorChild: HTMLAnchorElement, domEventTarget: org.w3c.dom.HTMLSpanElement) : HTMLSpanElement(domEventTarget), HTMLHyperlinkElementUtils {

    actual val disabled: Boolean
        get() = this.childNodes.contains(this.anchorChild)

    override val href: String?
        get() = this.anchorChild.href

    actual fun disable() {
        if (!this.disabled)
            this.removeChild(this.anchorChild)
    }

    actual fun enable(href: String?) {
        if(this.disabled && (this.href != null || href != null)){
            if (href != null)
                this.anchorChild = HTMLAnchorElement.create(href)

            this.appendChild(this.anchorChild)
        }
    }

    actual companion object {
        actual fun create(id: String?, classList: Set<String>, href: String, disabled: Boolean, hidden: Boolean?, onclick: String?): HTMLLinkButtonElement = HTMLAnchorElement.create(href).let { anchorElm ->
            HTMLLinkButtonElement (
                anchorChild = anchorElm,
                domEventTarget = document.create.span {
                    if (id != null)
                        this.id = id
                    classes = classList
                    if (hidden != null)
                        this.hidden = hidden
                    if (onclick != null)
                        this.onClick = onclick
                }
            ).let {
                if (!disabled)
                    it.enable()
                it
            }
        }

        actual fun create(id: String?, classList: Set<String>, href: String?, hidden: Boolean?, onclick: String?): HTMLLinkButtonElement = HTMLLinkButtonElement (
            anchorChild = HTMLAnchorElement.create(href),
            domEventTarget = document.create.span {
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