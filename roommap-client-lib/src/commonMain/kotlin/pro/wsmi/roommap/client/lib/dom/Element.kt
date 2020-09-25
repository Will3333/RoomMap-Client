package pro.wsmi.roommap.client.lib.dom

abstract class Element: Node(), ParentNode, ChildNode, NonDocumentTypeChildNode
{
    abstract val tagName: String
    abstract val id: String?
    abstract val classList: List<String>

    @ExperimentalUnsignedTypes
    override val nodeType: NodeType = NodeType.ELEMENT_NODE
    override val nodeName: String
        get() = this.tagName

    override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}