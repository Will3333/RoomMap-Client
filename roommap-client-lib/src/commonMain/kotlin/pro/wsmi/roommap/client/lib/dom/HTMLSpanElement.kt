package pro.wsmi.roommap.client.lib.dom

expect open class HTMLSpanElement : HTMLElement
{
    companion object {
        fun create (
            id: String? = null,
            classList: Set<String> = setOf(),
            hidden: Boolean? = null,
            onclick: String? = null
        ) : HTMLSpanElement
    }
}