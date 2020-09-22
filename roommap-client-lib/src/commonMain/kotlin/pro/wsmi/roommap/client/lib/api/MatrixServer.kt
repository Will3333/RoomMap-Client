package pro.wsmi.roommap.client.lib.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatrixServer (
    @SerialName("name")
    val name: String,
    @SerialName("api_url")
    val apiUrl: String,
    @SerialName("update_freq")
    val updateFreq: Long
)