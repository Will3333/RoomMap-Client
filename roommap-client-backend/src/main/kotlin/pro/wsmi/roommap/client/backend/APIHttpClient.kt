/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.backend

import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import pro.wsmi.kwsmilib.net.URL

@ExperimentalSerializationApi
fun getAPIHttpRequestBase(userAgent: String, apiUrl: URL) : Request
{
    val initReq = Request(Method.GET, apiUrl.toString())

    val reqWithUAHeader = initReq.header("User-Agent", userAgent)
    val reqWithCTHeader = reqWithUAHeader.header("Content-Type", ContentType.APPLICATION_JSON.toHeaderValue())
    val reqWithACTHeader = reqWithCTHeader.header("Accept", ContentType.APPLICATION_JSON.toHeaderValue())
    return reqWithACTHeader.header("Accept-Charset", "utf-8")
}