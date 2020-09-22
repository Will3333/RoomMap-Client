package pro.wsmi.roommap.client.backend.config

import kotlinx.serialization.Serializable

@Serializable
data class ClientBackend (
    val port: Int = 80,
    val compression: Boolean
)