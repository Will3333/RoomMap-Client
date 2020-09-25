package pro.wsmi.roommap.client.lib.dom

actual abstract class CharacterData actual constructor() : Node(), ChildNode, NonDocumentTypeChildNode
{
    actual abstract val data: String

    @ExperimentalUnsignedTypes
    actual val length: ULong
        get() = this.data.length.toULong()
    actual override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    actual override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}