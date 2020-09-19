package pro.wsmi.roommap.client.http_server

import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import pro.wsmi.kwsmilib.net.URL

@ExperimentalSerializationApi
fun getAPIHttpRequestBase(userAgent: String, apiUrl: URL) : Request
{
    val initReq = Request(Method.GET, apiUrl.toString())

    val reqWithUAHeader = initReq.header("User-Agent", userAgent)
    val reqWithCTHeader = reqWithUAHeader.header("Content-Type", ContentType.APPLICATION_JSON.toHeaderValue())
    val reqWithACTHeader = reqWithCTHeader.header("Accept", ContentType.APPLICATION_JSON.toHeaderValue())
    return reqWithACTHeader.header("Accept-Charset", "utf-8")
}