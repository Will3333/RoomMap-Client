package pro.wsmi.roommap.client.jvm.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import pro.wsmi.kwsmilib.jvm.serialization.InetSocketAddressSerializer
import java.net.InetSocketAddress

@ExperimentalSerializationApi
@Serializable
data class APIHttpServer (
    @Serializable(with = InetSocketAddressSerializer::class)
    val server: InetSocketAddress,
    val tls: Boolean,
    val compression: Boolean
)