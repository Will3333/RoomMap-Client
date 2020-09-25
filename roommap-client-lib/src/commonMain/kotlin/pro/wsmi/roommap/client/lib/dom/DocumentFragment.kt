package pro.wsmi.roommap.client.lib.dom

class DocumentFragment : Node(), ParentNode
{
    override val nodeName: String = "#document-fragment"
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType = NodeType.DOCUMENT_FRAGMENT_NODE
}