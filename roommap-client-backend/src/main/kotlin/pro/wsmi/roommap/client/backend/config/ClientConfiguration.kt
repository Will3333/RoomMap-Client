/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
    val clientHttpServer: ClientHttpServerConfiguration,
    @SerialName("resource_directory")
    @Serializable(with = FileSerializer::class)
    val resourceDirectory: File,
    @SerialName("public_api_data_update_frequency")
    val publicAPIDataUpdateFrequency: Long,
    @SerialName("freemarker_template_version")
    val freeMarkerTemplateVersion: String,
    @SerialName("website_name")
    val websiteName: String
)