package pro.wsmi.roommap.client.lib.dom

expect class HTMLAnchorElement (
    id: String? = null,
    classList: Set<String> = setOf(),
    href: String? = null,
    hidden: Boolean? = null,
    onclick: String? = null
) : HTMLElement, HTMLHyperlinkElementUtils
{
    override val href: String?
    override val onclick: String?
}