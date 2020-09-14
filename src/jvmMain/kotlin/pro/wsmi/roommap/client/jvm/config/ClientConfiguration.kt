package pro.wsmi.roommap.client.jvm.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pro.wsmi.kwsmilib.jvm.serialization.FileSerializer
import java.io.File

@ExperimentalSerializationApi
@Serializable
data class ClientConfiguration (
    @SerialName("instance_name")
    val instanceName: String,
    @SerialName("api_http_server")
    val apiHttpServer: APIHttpServer,
    @SerialName("client_http_server")
    val clientHttpServer: ClientHttpServer,
    @SerialName("resource_directory")
    @Serializable(with = FileSerializer::class)
    val resourceDirectory: File,
    @SerialName("freemarker_template_version")
    val freeMarkerTemplateVersion: String
)