package pro.wsmi.roommap.client.http_server.config

import kotlinx.serialization.Serializable

@Serializable
data class ClientHttpServer (
    val port: Int = 80,
    val compression: Boolean
)