package pro.wsmi.roommap.client.lib.dom

expect abstract class Node : EventTarget
{
    var parentNode: Node?
        private set
    var childNodes: List<Node>
        private set

    abstract val nodeName: String
    @ExperimentalUnsignedTypes
    abstract val nodeType: NodeType

    val previousSibling: Node?
    val nextSibling: Node?

    fun appendChild(child: Node) : Node?
    fun removeChild(child: Node) : Node?
}