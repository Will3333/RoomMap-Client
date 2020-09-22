package pro.wsmi.roommap.client.backend.http4k

import org.http4k.core.ContentType

val ContentType.Companion.TEXT_CSS: ContentType
    get() = ContentType.Text("text/css")
val ContentType.Companion.APPLICATION_JS: ContentType
    get() = ContentType.Text("application/javascript")