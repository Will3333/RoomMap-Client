package pro.wsmi.roommap.client.lib.dom

actual class HTMLDivElement actual constructor(
    actual override val id: String?,
    actual override val classList: Set<String>,
    actual override val onclick: String?
) : HTMLElement() {
    actual override val tagName: String = "div"
}