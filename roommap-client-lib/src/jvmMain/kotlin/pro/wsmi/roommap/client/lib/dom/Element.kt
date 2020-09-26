package pro.wsmi.roommap.client.lib.dom

actual abstract class Element : Node(), ParentNode, ChildNode, NonDocumentTypeChildNode
{
    actual open val tagName: String = ""
    actual open val id: String? = null
    actual open val classList: Set<String> = setOf()

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType = NodeType.ELEMENT_NODE
    actual override val nodeName: String
        get() = this.tagName
    actual override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    actual override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}