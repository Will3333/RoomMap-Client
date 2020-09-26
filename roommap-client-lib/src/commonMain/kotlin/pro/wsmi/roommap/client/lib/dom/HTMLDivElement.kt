package pro.wsmi.roommap.client.lib.dom

expect class HTMLDivElement (
    id: String? = null,
    classList: Set<String> = setOf(),
    onclick: String? = null
): HTMLElement
{
    override val onclick: String?
}