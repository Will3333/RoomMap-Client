package pro.wsmi.roommap.client.lib.dom

@ExperimentalUnsignedTypes
enum class NodeType constructor(value: UShort)
{
    ELEMENT_NODE(1u),
    TEXT_NODE(3u),
    PROCESSING_INSTRUCTION_NODE(7u),
    COMMENT_NODE(8u),
    DOCUMENT_NODE(9u),
    DOCUMENT_TYPE_NODE(10u),
    DOCUMENT_FRAGMENT_NODE(11u);

    companion object {
        fun getNodeType(value: UShort) : NodeType? = when(value.toUInt())
        {
            1u -> ELEMENT_NODE
            3u -> TEXT_NODE
            7u -> PROCESSING_INSTRUCTION_NODE
            8u -> COMMENT_NODE
            9u -> DOCUMENT_NODE
            10u -> DOCUMENT_TYPE_NODE
            11u -> DOCUMENT_FRAGMENT_NODE
            else -> null
        }
    }
}