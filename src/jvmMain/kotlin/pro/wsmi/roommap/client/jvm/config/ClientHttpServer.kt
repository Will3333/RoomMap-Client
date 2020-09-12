package pro.wsmi.roommap.client.jvm.config

import kotlinx.serialization.Serializable

@Serializable
data class ClientHttpServer (
    val port: Int = 80,
    val compression: Boolean
)