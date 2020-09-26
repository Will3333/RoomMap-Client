package pro.wsmi.roommap.client.lib.dom

actual abstract class HTMLElement actual constructor() : Element(), GlobalEventHandlers
{
    actual open val hidden: Boolean?
        get() = (this.domEventTarget as org.w3c.dom.HTMLElement).hidden
    override val onclick: String?
        get() = (this.domEventTarget as org.w3c.dom.HTMLElement).onclick.toString()
}