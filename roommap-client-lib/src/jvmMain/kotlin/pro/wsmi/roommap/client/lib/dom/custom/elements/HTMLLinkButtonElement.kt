package pro.wsmi.roommap.client.lib.dom.custom.elements

import pro.wsmi.roommap.client.lib.dom.HTMLAnchorElement
import pro.wsmi.roommap.client.lib.dom.HTMLHyperlinkElementUtils
import pro.wsmi.roommap.client.lib.dom.HTMLSpanElement

actual open class HTMLLinkButtonElement protected constructor (
    protected var anchorChild: HTMLAnchorElement,
    id: String?,
    classList: Set<String>,
    hidden: Boolean?,
    onclick: String?
) : HTMLSpanElement(id, classList, hidden, onclick), HTMLHyperlinkElementUtils
{
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
        actual fun create(id: String?, classList: Set<String>, href: String, disabled: Boolean, hidden: Boolean?, onclick: String?): HTMLLinkButtonElement = HTMLAnchorElement.create(href).let { anchorChild ->
            HTMLLinkButtonElement(
                anchorChild = anchorChild,
                id = id,
                classList = classList,
                hidden = hidden,
                onclick = onclick
            ).let {
                if (!disabled)
                    it.enable()
                it
            }
        }

        actual fun create(id: String?, classList: Set<String>, href: String?, hidden: Boolean?, onclick: String?): HTMLLinkButtonElement = HTMLLinkButtonElement(
            anchorChild = HTMLAnchorElement.create(href),
            id = id,
            classList = classList,
            hidden = hidden,
            onclick = onclick
        )
    }
}