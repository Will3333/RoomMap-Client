package pro.wsmi.roommap.client.lib.js

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
fun getAPIHttpClient(userAgent: String, apiHost: Url): HttpClient = HttpClient(Js) {

    install(UserAgent) {
        agent = userAgent
    }
    Charsets {
        register(io.ktor.utils.io.charsets.Charsets.UTF_8)
    }
    defaultRequest {
        url {
            protocol = apiHost.protocol
        }
        host = apiHost.host
        port = apiHost.port
        accept(ContentType.Application.Json)
    }
}
