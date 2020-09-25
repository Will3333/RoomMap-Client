package pro.wsmi.roommap.client.lib.dom

abstract class Node : EventTarget
{
    var parentNode: Node? = null
        private set
    var childNodes: List<Node> = listOf()
        private set

    abstract val nodeName: String
    @ExperimentalUnsignedTypes
    abstract val nodeType: NodeType

    val previousSibling: Node?
        get() = if (this.parentNode?.childNodes != null) {
            this.parentNode!!.childNodes.indexOf(this).let { nodeIndex: Int ->
                if (nodeIndex > 0) this.parentNode!!.childNodes[nodeIndex-1]
                else null
            }
        }
        else null
    val nextSibling: Node?
        get() = if (this.parentNode?.childNodes != null) {
            this.parentNode!!.childNodes.indexOf(this).let { nodeIndex: Int ->
                if (nodeIndex >= 0 && nodeIndex != this.parentNode!!.childNodes.lastIndex)
                    this.parentNode!!.childNodes[nodeIndex+1]
                else null
            }
        }
        else null

    fun appendChild(child: Node) : Node? = when(child)
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

    fun removeChild(child: Node) : Node? = if (this.childNodes.contains(child))
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
}