package pro.wsmi.roommap.client.matrix_rooms_page

import kotlinx.browser.document
import kotlinx.html.dom.create
import kotlinx.html.js.a
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList

class LinkButton(val htmlElm: HTMLElement, val disabledClassName: String) : pro.wsmi.roommap.client.lib.dom.HTMLElement()
{
    val btnText = if (htmlElm.classList.contains(disabledClassName))
        this.htmlElm.innerText
    else
        this.htmlElm.getElementsByTagName("a").asList()[0].innerHTML

    var url = this.htmlElm.getElementsByTagName("a").asList().let {
        if (it.isEmpty())
            null
        else
            (it[0] as HTMLAnchorElement).href
    }

    fun disable()
    {
        this.htmlElm.classList.add(this.disabledClassName)
        this.htmlElm.removeChild(this.htmlElm.getElementsByTagName("a").asList()[0])
        this.htmlElm.innerText = this.btnText
    }

    fun enable(newUrl: String? = null)
    {
        if (newUrl != null)
            this.url = newUrl

        if (this.url != null)
        {
            document.create.a {
                href = this@LinkButton.url!!
                + this@LinkButton.btnText
            }.let {
                this.htmlElm.appendChild(it)
            }
        }
    }
}