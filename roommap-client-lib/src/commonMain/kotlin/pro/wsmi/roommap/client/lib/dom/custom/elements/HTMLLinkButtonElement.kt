package pro.wsmi.roommap.client.lib.dom.custom.elements

import pro.wsmi.roommap.client.lib.dom.HTMLHyperlinkElementUtils
import pro.wsmi.roommap.client.lib.dom.HTMLSpanElement


expect open class HTMLLinkButtonElement : HTMLSpanElement, HTMLHyperlinkElementUtils
{
    val disabled: Boolean

    fun disable()
    fun enable(href: String? = null)

    companion object {
        fun create(
            id: String? = null,
            classList: Set<String> = setOf(),
            href: String,
            disabled: Boolean = false,
            hidden: Boolean? = null,
            onclick: String? = null
        ) : HTMLLinkButtonElement
        fun create(
            id: String? = null,
            classList: Set<String> = setOf(),
            href: String? = null,
            hidden: Boolean? = null,
            onclick: String? = null
        ) : HTMLLinkButtonElement
    }
}