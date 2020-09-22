package pro.wsmi.roommap.client.lib.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientAPIServerListReqResponse (
    @SerialName("servers")
    val servers: Map<String, MatrixServer>
) : ClientAPIResponse