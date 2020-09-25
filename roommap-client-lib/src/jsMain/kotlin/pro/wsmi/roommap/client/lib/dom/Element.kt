package pro.wsmi.roommap.client.lib.dom

actual abstract class Element : Node(), ParentNode, ChildNode, NonDocumentTypeChildNode
{
    actual abstract val tagName: String
    actual abstract val id: String?
    actual abstract val classList: Set<String>

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType
        get() = NodeType.getNodeType((this.domEventTarget as org.w3c.dom.Element).nodeType.toUShort())!!
    actual override val nodeName: String
        get() = (this.domEventTarget as org.w3c.dom.Element).nodeName
    actual override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    actual override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}