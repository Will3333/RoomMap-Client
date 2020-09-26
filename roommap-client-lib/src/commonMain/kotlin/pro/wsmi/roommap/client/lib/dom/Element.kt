package pro.wsmi.roommap.client.lib.dom

expect abstract class Element(): Node, ParentNode, ChildNode, NonDocumentTypeChildNode
{
    open val tagName: String
    open val id: String?
    open val classList: Set<String>

    @ExperimentalUnsignedTypes
    override val nodeType: NodeType
    override val nodeName: String
    override val previousElementSibling: Element?
    override val nextElementSibling: Element?
}