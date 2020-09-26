package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document

actual class DocumentFragment : Node(), ParentNode
{
    override val domEventTarget: org.w3c.dom.DocumentFragment = document.createDocumentFragment()

    actual override val nodeName: String
        get() = this.domEventTarget.nodeName

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType
        get() = NodeType.getNodeType(this.domEventTarget.nodeType.toUShort())!!
}