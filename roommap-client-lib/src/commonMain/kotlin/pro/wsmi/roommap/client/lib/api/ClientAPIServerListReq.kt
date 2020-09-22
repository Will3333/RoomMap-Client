package pro.wsmi.roommap.client.lib.api

class ClientAPIServerListReq : ClientAPIRequest
{
    companion object {
        const val REQ_PATH = ClientAPIRequest.API_PATH + "/servers"
    }

    override fun getReqPath(): String = REQ_PATH
}