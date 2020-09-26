package pro.wsmi.roommap.client.lib.dom

actual abstract class Node actual constructor() : EventTarget()
{
    actual var parentNode: Node? = null
    var parentDomNode: org.w3c.dom.Node? = null
        internal set
    actual var childNodes: List<Node> = listOf()

    actual abstract val nodeName: String
    @ExperimentalUnsignedTypes
    actual abstract val nodeType: NodeType

    actual val previousSibling: Node?
        get() = if (this.parentNode?.childNodes != null) {
            this.parentNode!!.childNodes.indexOf(this).let { nodeIndex: Int ->
                if (nodeIndex > 0) this.parentNode!!.childNodes[nodeIndex-1]
                else null
            }
        }
        else null
    actual val nextSibling: Node?
        get() = if (this.parentNode?.childNodes != null) {
            this.parentNode!!.childNodes.indexOf(this).let { nodeIndex: Int ->
                if (nodeIndex >= 0 && nodeIndex != this.parentNode!!.childNodes.lastIndex)
                    this.parentNode!!.childNodes[nodeIndex+1]
                else null
            }
        }
        else null

    actual fun appendChild(child: Node): Node? = when(child)
    {
        is ChildNode, is DocumentFragment -> {
            if (child.parentNode == null && child.parentDomNode == null) {
                val domNode = this.domEventTarget as org.w3c.dom.Node
                domNode.appendChild(child.domEventTarget as org.w3c.dom.Node)
                when(child) {
                    is ChildNode -> {
                        this.childNodes = this.childNodes.toMutableList().let {
                            it.add(child)
                            it.toList()
                        }
                        child.parentNode = this
                        child
                    }
                    is DocumentFragment -> {
                        this.childNodes = this.childNodes.toMutableList().let {
                            it.addAll(child.childNodes)
                            child.childNodes = listOf()
                            it.toList()
                        }
                        child.childNodes = listOf()
                        child
                    }
                    else -> null
                }
            }
            else null
        }
        else -> null
    }

    actual fun removeChild(child: Node): Node? = if (this.childNodes.contains(child))
    {
        val domNode = this.domEventTarget as org.w3c.dom.Node
        domNode.removeChild(child.domEventTarget as org.w3c.dom.Node)

        this.childNodes = this.childNodes.toMutableList().let {
            it.remove(child)
            it.toList()
        }
        child.parentNode = null
        child
    }
    else
        null
}