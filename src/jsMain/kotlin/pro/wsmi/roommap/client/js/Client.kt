package pro.wsmi.roommap.client.js

import kotlinx.serialization.ExperimentalSerializationApi
import pro.wsmi.kwsmilib.net.URL
import pro.wsmi.roommap.client.USER_AGENT

external val apiUrlStr: String
external val debugMode: Boolean

@ExperimentalSerializationApi
fun main()
{
    val apiUrl = URL.parseURL(apiUrlStr)
    if(debugMode && apiUrl == null)
        println("The API url was not found or has a wrong format")

    val apiHttpClient = if (apiUrl != null)
        getAPIHttpClient(USER_AGENT, apiUrl)
    else {
        if (debugMode)
            println("The API url was not found or has a wrong format")
        null
    }


}