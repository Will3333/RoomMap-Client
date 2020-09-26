package pro.wsmi.roommap.client.lib.dom

actual abstract class EventTarget {
    internal abstract val domEventTarget: org.w3c.dom.events.EventTarget
}