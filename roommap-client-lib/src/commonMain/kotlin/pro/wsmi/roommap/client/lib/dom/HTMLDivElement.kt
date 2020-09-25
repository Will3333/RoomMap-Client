package pro.wsmi.roommap.client.lib.dom

data class HTMLDivElement (
    override val id: String? = null,
    override val classList: List<String> = listOf(),
    override val onclick: String? = null
) : HTMLElement()
{
    override val tagName: String = "div"
}