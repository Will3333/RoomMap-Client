package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document

actual open class Text protected constructor(domEventTarget: org.w3c.dom.Text) : CharacterData(domEventTarget)
{
    override val data: String
        get() = (this.domEventTarget as org.w3c.dom.Text).data

    @ExperimentalUnsignedTypes
    override val nodeType: NodeType
        get() = NodeType.getNodeType((this.domEventTarget as org.w3c.dom.Text).nodeType.toUShort())!!
    override val nodeName: String
        get() = (this.domEventTarget as org.w3c.dom.Text).nodeName
    actual val wholeText: String
        get() = (this.domEventTarget as org.w3c.dom.Text).wholeText

    actual val previousTextSibling: Text?
        get() = this.previousSibling.let {
            if (it != null && it is Text) it else null
        }
    actual val nextTextSibling: Text?
        get() = this.nextSibling.let {
            if (it != null && it is Text) it else null
        }

    actual companion object {
        actual fun create(data: String): Text = Text(document.createTextNode(data))
    }
}