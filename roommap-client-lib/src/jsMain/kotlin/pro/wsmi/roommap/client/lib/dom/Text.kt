package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document

actual class Text actual constructor(data: String) : CharacterData()
{
    override val domEventTarget: org.w3c.dom.Text = document.createTextNode(data)

    actual override val data: String
        get() = this.domEventTarget.data

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType
        get() = NodeType.getNodeType(this.domEventTarget.nodeType.toUShort())!!
    actual override val nodeName: String
        get() = this.domEventTarget.nodeName
    actual val wholeText: String
        get() = this.domEventTarget.wholeText

    actual val previousTextSibling: Text?
        get() = this.previousSibling.let {
            if (it != null && it is Text) it else null
        }
    actual val nextTextSibling: Text?
        get() = this.nextSibling.let {
            if (it != null && it is Text) it else null
        }
}