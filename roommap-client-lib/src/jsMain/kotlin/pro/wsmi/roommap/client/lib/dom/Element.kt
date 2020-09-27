package pro.wsmi.roommap.client.lib.dom

import org.w3c.dom.asList

actual abstract class Element(domEventTarget: org.w3c.dom.Element) : Node(domEventTarget), ElementInterface, ParentNode, ChildNode, NonDocumentTypeChildNode
{
    override val tagName: String
        get() = (this.domEventTarget as org.w3c.dom.Element).tagName
    override val id: String?
        get() = (this.domEventTarget as org.w3c.dom.Element).id
    override val classList: Set<String>
        get() = (this.domEventTarget as org.w3c.dom.Element).classList.asList().toSet()

    @ExperimentalUnsignedTypes
    actual override val nodeType: NodeType
        get() = NodeType.getNodeType((this.domEventTarget as org.w3c.dom.Element).nodeType.toUShort())!!
    actual override val nodeName: String
        get() = (this.domEventTarget as org.w3c.dom.Element).nodeName
    actual override val previousElementSibling: Element?
        get() = this.previousSibling.let {
            if (it != null && it is Element) it else null
        }
    actual override val nextElementSibling: Element?
        get() = this.nextSibling.let {
            if (it != null && it is Element) it else null
        }
}