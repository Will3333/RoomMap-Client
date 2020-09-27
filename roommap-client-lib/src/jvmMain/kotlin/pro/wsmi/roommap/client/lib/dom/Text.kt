package pro.wsmi.roommap.client.lib.dom

actual open class Text protected constructor(override val data: String) : CharacterData()
{
    @ExperimentalUnsignedTypes
    override val nodeType: NodeType = NodeType.TEXT_NODE
    override val nodeName: String = "#text"

    actual val previousTextSibling: Text?
        get() = this.previousSibling.let {
            if (it != null && it is Text) it else null
        }
    actual val nextTextSibling: Text?
        get() = this.nextSibling.let {
            if (it != null && it is Text) it else null
        }

    actual val wholeText: String
        get()
        {
            var wholeText = this.data

            this.parentNode?.childNodes.let { childNodes ->

                if (childNodes != null)
                {
                    val thisNodeIndex = childNodes.indexOf(this)

                    if (thisNodeIndex > 0) {
                        for (index in thisNodeIndex-1 downTo 0)
                        {
                            val node = childNodes[index]
                            if (node is Text)
                                wholeText = node.data + wholeText
                            else
                                break
                        }
                    }
                    if (thisNodeIndex < childNodes.lastIndex) {
                        for (index in thisNodeIndex+1..(childNodes.lastIndex))
                        {
                            val node = childNodes[index]
                            if (node is Text)
                                wholeText += node.data
                            else
                                break
                        }
                    }
                }
            }

            return wholeText
        }

    actual companion object {
        actual fun create(data: String): Text = Text(data)
    }
}