package pro.wsmi.roommap.client.lib.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatrixRoom (
    @SerialName("room_id")
    val roomId: String,
    @SerialName("server_id")
    val serverId: String,
    @SerialName("aliases")
    val aliases: List<String>? = null,
    @SerialName("canonical_alias")
    val canonicalAlias: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("num_joined_members")
    val numJoinedMembers: Int,
    @SerialName("topic")
    val topic: String? = null,
    @SerialName("world_readable")
    val worldReadable: Boolean,
    @SerialName("guest_can_join")
    val guestCanJoin: Boolean,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)