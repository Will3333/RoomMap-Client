package pro.wsmi.roommap.client.lib.dom

expect abstract class Element: Node, ElementInterface, ParentNode, ChildNode, NonDocumentTypeChildNode
{
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType
    override val nodeName: String
    override val previousElementSibling: Element?
    override val nextElementSibling: Element?
}