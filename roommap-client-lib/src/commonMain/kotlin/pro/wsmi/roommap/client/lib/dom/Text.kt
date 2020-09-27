package pro.wsmi.roommap.client.lib.dom

expect open class Text : CharacterData
{
    val previousTextSibling: Text?
    val nextTextSibling: Text?
    val wholeText: String

    companion object {
        fun create(data: String) : Text
    }
}