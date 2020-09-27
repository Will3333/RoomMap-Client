package pro.wsmi.roommap.client.lib.dom

actual abstract class HTMLElement : Element(), HTMLElementInterface, GlobalEventHandlers
{
    abstract fun toHTMLString(): String
}