package pro.wsmi.roommap.client.js

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import pro.wsmi.kwsmilib.net.URL

@ExperimentalSerializationApi
fun getAPIHttpClient(userAgent: String, apiURL: URL): HttpClient = HttpClient(Js) {

    install(UserAgent) {
        agent = userAgent
    }
    Charsets {
        register(io.ktor.utils.io.charsets.Charsets.UTF_8)
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        url(Url(apiURL.toString()))
    }
}
