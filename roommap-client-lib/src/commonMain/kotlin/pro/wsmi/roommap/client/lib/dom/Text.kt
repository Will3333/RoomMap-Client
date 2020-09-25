package pro.wsmi.roommap.client.lib.dom

expect class Text(
    data: String
) : CharacterData
{
    override val data: String
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType
    override val nodeName: String

    val previousTextSibling: Text?
    val nextTextSibling: Text?
    val wholeText: String
}