package pro.wsmi.roommap.client.lib.dom

expect open class HTMLAnchorElement : HTMLElement, HTMLHyperlinkElementUtils
{
    companion object {
        fun create (
            id: String? = null,
            classList: Set<String> = setOf(),
            href: String? = null,
            hidden: Boolean? = null,
            onclick: String? = null
        ) : HTMLAnchorElement
    }
}