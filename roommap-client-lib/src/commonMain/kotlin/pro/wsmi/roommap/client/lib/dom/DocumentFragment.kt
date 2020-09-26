package pro.wsmi.roommap.client.lib.dom

expect class DocumentFragment() : Node, ParentNode
{
    override val nodeName: String
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType
}