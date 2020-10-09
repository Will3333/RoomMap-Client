package pro.wsmi.roommap.client.backend

interface QueryParameters {
    fun toUrlFormat() : String
    fun toOwnFormat (pattern: String): String
}