package pro.wsmi.roommap.client.lib.dom

expect open class DocumentFragment : Node, ParentNode
{
    override val nodeName: String
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType

    companion object {
        fun create() : DocumentFragment
    }
}