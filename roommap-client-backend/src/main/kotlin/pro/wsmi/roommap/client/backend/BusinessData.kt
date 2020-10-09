package pro.wsmi.roommap.client.backend

import kotlinx.serialization.ExperimentalSerializationApi
import pro.wsmi.roommap.lib.api.MatrixRoom
import pro.wsmi.roommap.lib.api.MatrixServer

@ExperimentalSerializationApi
data class BusinessData (
    var matrixServers: Map<String, MatrixServer> = mutableMapOf(),
    var matrixRooms: List<MatrixRoom> = mutableListOf()
)