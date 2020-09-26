package pro.wsmi.roommap.client.lib.dom

expect class HTMLDivElement (
    id: String? = null,
    classList: Set<String> = setOf(),
    hidden: Boolean? = null,
    onclick: String? = null
): HTMLElement
{
    override val onclick: String?
}