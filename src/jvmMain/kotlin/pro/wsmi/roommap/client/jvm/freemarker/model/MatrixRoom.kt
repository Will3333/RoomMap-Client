package pro.wsmi.roommap.client.jvm.freemarker.model

data class MatrixRoom (
    val room_id: String,
    val server: MatrixServer,
    val aliases: List<String>? = null,
    val canonical_alias: String? = null,
    val name: String? = null,
    val num_joined_members: Int,
    val topic: String? = null,
    val world_readable: Boolean,
    val guest_can_join: Boolean,
    val avatar_url: String? = null
)