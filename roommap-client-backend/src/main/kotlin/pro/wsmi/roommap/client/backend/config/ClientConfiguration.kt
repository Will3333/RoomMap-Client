package pro.wsmi.roommap.client.backend.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pro.wsmi.kwsmilib.jvm.serialization.FileSerializer
import pro.wsmi.kwsmilib.net.URL
import java.io.File

@ExperimentalSerializationApi
@Serializable
data class ClientConfiguration (
    @SerialName("instance_name")
    val instanceName: String,
    @SerialName("api_url")
    val apiURL: URL,
    @SerialName("client_backend")
    val clientHttpServer: ClientBackend,
    @SerialName("resource_directory")
    @Serializable(with = FileSerializer::class)
    val resourceDirectory: File,
    @SerialName("freemarker_template_version")
    val freeMarkerTemplateVersion: String
)