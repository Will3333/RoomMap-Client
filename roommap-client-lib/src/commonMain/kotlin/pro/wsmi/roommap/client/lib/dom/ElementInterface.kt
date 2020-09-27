package pro.wsmi.roommap.client.lib.dom

interface ElementInterface {
    val tagName: String
    val id: String?
    val classList: Set<String>
}