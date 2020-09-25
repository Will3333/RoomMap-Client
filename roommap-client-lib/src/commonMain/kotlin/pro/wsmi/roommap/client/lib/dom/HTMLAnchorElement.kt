package pro.wsmi.roommap.client.lib.dom

data class HTMLAnchorElement (
    override val id: String? = null,
    override val classList: List<String> = listOf(),
    override val href: String? = null,
    override val onclick: String? = null
) : HTMLElement(), HTMLHyperlinkElementUtils
{
    override val tagName: String = "a"
}