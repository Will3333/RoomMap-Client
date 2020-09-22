package pro.wsmi.roommap.client.lib.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientAPIRoomListReqResponse (
    @SerialName("rooms")
    val rooms: List<MatrixRoom>
) : ClientAPIResponse