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
data class Page404FreeMarkerDataModel (
    override val globalData: GlobalFreeMarkerDataModel,
    override val cssFileNames: List<String>?,
    override val texts: ResourceBundle
) : PageFreeMarkerDataModel
{
    override val templateFileName: String = PAGE_404_TEMPLATE_FILE_NAME
    override val urlPath: String = ""
    override val robotsMeta: RobotsMeta? = null
    override val jsFileNames: List<String>? = null
    override val queryParameters = Page404QueryParameters()
}