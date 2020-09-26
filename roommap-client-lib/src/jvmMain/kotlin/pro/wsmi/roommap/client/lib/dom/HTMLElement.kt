package pro.wsmi.roommap.client.lib.dom

actual abstract class HTMLElement actual constructor() : Element(), GlobalEventHandlers
{
    abstract fun toHTMLString(): String
}