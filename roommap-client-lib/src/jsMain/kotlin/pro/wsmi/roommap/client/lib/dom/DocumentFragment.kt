package pro.wsmi.roommap.client.lib.dom

import kotlinx.browser.document

actual open class DocumentFragment protected constructor(domEventTarget: org.w3c.dom.DocumentFragment) : Node(domEventTarget), ParentNode
{
    actual override val nodeName: String
        get() = (this.domEventTarget as org.w3c.dom.DocumentFragment).nodeName

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType
        get() = NodeType.getNodeType((this.domEventTarget as org.w3c.dom.DocumentFragment).nodeType.toUShort())!!

    actual companion object {
        actual fun create(): DocumentFragment = DocumentFragment(document.createDocumentFragment())
    }
}