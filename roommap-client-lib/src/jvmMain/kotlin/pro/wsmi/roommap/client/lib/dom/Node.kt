package pro.wsmi.roommap.client.lib.dom

actual abstract class Node : EventTarget()
{
    actual var parentNode: Node? = null
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

    actual fun appendChild(child: Node): Node? = if (child.parentNode == null)
    {
        when(child)
        {
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
                    it.toList()
                }
                child.childNodes = listOf()
                child
            }
            else -> null
        }
    }
    else null

    actual fun removeChild(child: Node): Node? = if (this.childNodes.contains(child))
    {
        this.childNodes = this.childNodes.toMutableList().let {
            it.remove(child)
            it.toList()
        }
        child.parentNode = null
        child
    }
    else
        null


    fun getChildrenHTMLString(): String = this.childNodes.joinToString(separator = "") {
        when (it) {
            is Text -> it.data
            is HTMLElement -> it.toHTMLString()
            else -> ""
        }
    }
}