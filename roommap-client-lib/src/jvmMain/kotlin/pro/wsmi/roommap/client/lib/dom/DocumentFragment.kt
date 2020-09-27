package pro.wsmi.roommap.client.lib.dom

actual open class DocumentFragment : Node(), ParentNode
{
    actual override val nodeName: String = "#document-fragment"
    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType = NodeType.DOCUMENT_FRAGMENT_NODE

    actual companion object {
        actual fun create(): DocumentFragment = DocumentFragment()
    }
}