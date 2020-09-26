package pro.wsmi.roommap.client.lib.dom

actual class DocumentFragment actual constructor() : Node(), ParentNode
{
    actual override val nodeName: String = "#document-fragment"
    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType = NodeType.DOCUMENT_FRAGMENT_NODE
}