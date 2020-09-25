package pro.wsmi.roommap.client.lib.dom

actual abstract class EventTarget {
    protected abstract val domEventTarget: org.w3c.dom.events.EventTarget
}