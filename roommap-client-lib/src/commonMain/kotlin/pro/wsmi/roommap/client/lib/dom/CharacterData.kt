package pro.wsmi.roommap.client.lib.dom

abstract class CharacterData : Node(), ChildNode, NonDocumentTypeChildNode
{
    abstract val data: String

    @ExperimentalUnsignedTypes
    val length: ULong
        get() = this.data.length.toULong()

    override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}