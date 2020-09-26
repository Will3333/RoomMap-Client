package pro.wsmi.roommap.client.lib.dom

expect abstract class HTMLElement() : Element, GlobalEventHandlers
{
    open val hidden: Boolean?
}