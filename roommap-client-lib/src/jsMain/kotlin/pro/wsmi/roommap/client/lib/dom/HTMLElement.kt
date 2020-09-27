package pro.wsmi.roommap.client.lib.dom

actual abstract class HTMLElement(domEventTarget: org.w3c.dom.HTMLElement) : Element(domEventTarget), HTMLElementInterface, GlobalEventHandlers
{
    override val hidden: Boolean?
        get() = (this.domEventTarget as org.w3c.dom.HTMLElement).hidden
    override val onclick: String?
        get() = (this.domEventTarget as org.w3c.dom.HTMLElement).onclick.toString()
}