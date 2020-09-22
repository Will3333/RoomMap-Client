package pro.wsmi.roommap.client.lib.api

interface ClientAPIRequest : ClientAPIMessage
{
    companion object {
        const val API_PATH = "/api"
    }

    abstract fun getReqPath() : String
}