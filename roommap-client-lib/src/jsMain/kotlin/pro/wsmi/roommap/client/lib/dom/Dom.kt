package pro.wsmi.roommap.client.lib.dom

fun org.w3c.dom.Node.appendChild(child: Node) : Node? {
    return if (child.parentNode == null && child.parentDomNode == null)
    {
        val domChild = child.domEventTarget as org.w3c.dom.Node
        this.appendChild(domChild)
        child.parentDomNode = this
        child
    }
    else null
}

fun org.w3c.dom.Node.removeChild(child: Node) : Node? {
    return if (child.parentDomNode == this)
    {
        this.removeChild(child.domEventTarget as org.w3c.dom.Node)
        child.parentDomNode = null
        child
    }
    else null
}