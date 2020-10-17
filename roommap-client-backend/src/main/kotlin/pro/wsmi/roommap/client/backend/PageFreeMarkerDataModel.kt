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
import java.util.*

@ExperimentalSerializationApi
interface PageFreeMarkerDataModel {
    val globalData: GlobalFreeMarkerDataModel
    val templateFileName: String
    val urlPath: String
    val robotsMeta: RobotsMeta?
    val cssFileNames: List<String>?
    val jsFileNames: List<String>?
    val texts: ResourceBundle
    val queryParameters: QueryParameters
}

data class RobotsMeta(val index: Boolean?, val followLinks: Boolean?)
{
    fun toHTMLFormat() : String
    {
        if (this.index == null && this.followLinks == null)
            return ""

        val contentAttribut = when(this.index) {
            true -> "index"
            false -> "noindex"
            else -> ""
        }.let {
            when(this.followLinks) {
                true -> if (it.isNotEmpty()) "$it, " else {""} + "follow"
                false -> if (it.isNotEmpty()) "$it, " else {""} + "nofollow"
                else -> it
            }
        }

        return "<meta name=\"robots\" content=\"$contentAttribut\">"
    }
}