package pro.wsmi.roommap.client.lib.api

class ClientAPIRoomListReq : ClientAPIRequest
{
    companion object {
        const val REQ_PATH = ClientAPIRequest.API_PATH + "/rooms"
    }

    override fun getReqPath(): String = REQ_PATH
}