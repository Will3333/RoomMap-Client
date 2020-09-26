package pro.wsmi.roommap.client.lib.dom

actual abstract class HTMLElement actual constructor() : Element(), GlobalEventHandlers
{
    actual open val hidden: Boolean? = null

    abstract fun toHTMLString(): String
}