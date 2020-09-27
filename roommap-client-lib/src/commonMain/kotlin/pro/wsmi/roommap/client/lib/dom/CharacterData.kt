package pro.wsmi.roommap.client.lib.dom

expect abstract class CharacterData : Node, ChildNode, NonDocumentTypeChildNode
{
    abstract val data: String

    @ExperimentalUnsignedTypes
    val length: ULong
    override val previousElementSibling: Element?
    override val nextElementSibling: Element?
}