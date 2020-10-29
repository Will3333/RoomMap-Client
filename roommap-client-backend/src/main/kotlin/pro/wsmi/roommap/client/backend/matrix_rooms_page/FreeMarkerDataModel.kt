/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.backend.matrix_rooms_page

import kotlinx.serialization.ExperimentalSerializationApi
import pro.wsmi.roommap.client.backend.GlobalFreeMarkerDataModel
import pro.wsmi.roommap.client.backend.PageFreeMarkerDataModel
import pro.wsmi.roommap.client.backend.RobotsMeta
import pro.wsmi.roommap.client.lib.matrix_rooms_page.MatrixRoomsPageCSSClasses
import pro.wsmi.roommap.client.lib.matrix_rooms_page.MatrixRoomsPageHTMLElmId
import pro.wsmi.roommap.lib.api.PublicAPIMatrixRoom
import pro.wsmi.roommap.lib.api.PublicAPIMatrixServer
import java.util.*

@ExperimentalSerializationApi
data class FreeMarkerDataModel (
    override val globalData: GlobalFreeMarkerDataModel,
    override val cssFileNames: List<String>,
    override val jsFileNames: List<String>,
    override val texts: ResourceBundle,
    override val queryParameters: QueryParameters,
    val matrixRoomsData: MatrixRoomsData

) : PageFreeMarkerDataModel
{
    override val templateFileName: String = PAGE_TEMPLATE_FILE_NAME
    override val urlPath: String = "/"
    override val robotsMeta: RobotsMeta? = RobotsMeta(index = true, followLinks = true)
    val cssClasses = MatrixRoomsPageCSSClasses
    val htmlElmIds = MatrixRoomsPageHTMLElmId

    data class MatrixRoomsData (
        val pageMaxNum: Int,
        val totalRoomsNum: Int,
        val serverList: Map<String, PublicAPIMatrixServer>,
        val roomList : List<PublicAPIMatrixRoom>
    )
}