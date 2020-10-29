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

import freemarker.template.Template
import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.lens.*
import pro.wsmi.roommap.client.backend.BusinessData
import pro.wsmi.roommap.client.backend.GlobalFreeMarkerDataModel
import pro.wsmi.roommap.client.backend.http4k.asResult
import pro.wsmi.roommap.client.lib.matrix_rooms_page.*
import pro.wsmi.roommap.lib.api.*
import java.io.StringWriter
import java.util.*


internal const val PAGE_TEMPLATE_FILE_NAME = "matrix_rooms_page.ftlh"
private const val PAGE_CSS_FILE_NAME = "matrix_rooms_page.css"
private const val PAGE_NATIVE_JS_FILE_NAME = "roommap-client-js-matrix-rooms-page-native.js"
private const val PAGE_JS_FILE_NAME = "roommap-client-js-matrix-rooms-page.js"

private val getRootReqSorterQuery = Query.enum<MatrixRoomListSortingElement>().optional(MATRIX_ROOMS_PAGE_SORTER_REQ_NAME).asResult()
private val getRootReqSorterDirectionQuery = Query.boolean().optional(MATRIX_ROOMS_PAGE_SORTER_DIRECTION_REQ_NAME).asResult()
private val getRootReqMaxNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME).asResult()
private val getRootReqMinNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME).asResult()
private val getRootReqGAFilterQuery = Query.enum<MatrixRoomGuestCanJoinFilter>().optional(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME).asResult()
private val getRootReqWRFilterQuery = Query.enum<MatrixRoomWorldReadableFilter>().optional(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME).asResult()
private val getRootReqServerFilterQueries = Query.multi.optional(MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME)
private val getRootReqPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_PAGE_REQ_NAME).asResult()
private val getRootReqElmPerPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME).asResult()


@ExperimentalSerializationApi
fun handleMatrixRoomsPageReq(req: Request, freemarkerTemplate: Template, freeMarkerGlobalModelData: GlobalFreeMarkerDataModel, publicAPIData: BusinessData.PublicAPIData) : Response
{
    val sortingReq = getRootReqSorterQuery(req).getOrNull()
    val sortingDirectionReq = getRootReqSorterDirectionQuery(req).getOrNull()
    val gaFilteringReq = getRootReqGAFilterQuery(req).getOrNull()
    val wrFilteringReq = getRootReqWRFilterQuery(req).getOrNull()
    val serverFilteringReq = getRootReqServerFilterQueries(req)
    val maxNOUFilteringReq = getRootReqMaxNOUFilterQuery(req).getOrNull()
    val minNOUFilteringReq = getRootReqMinNOUFilterQuery(req).getOrNull()


    val filteredSortedMatrixRoomList = publicAPIData.matrixRooms.let {
        if (serverFilteringReq != null)
        {
            val newList = mutableListOf<PublicAPIMatrixRoom>()
            serverFilteringReq.forEach { serverId ->
                newList.addAll(it.filter { room ->
                    room.serverId == serverId
                })
            }
            newList.toList()
        }
        else
            it
    }.let {
        when(sortingReq) {
            MatrixRoomListSortingElement.ROOM_NAME -> {
                if (sortingDirectionReq != null && sortingDirectionReq) it.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { room ->
                    if (room.name.isNullOrBlank()) room.roomId else room.name!!
                })
                else it.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { room ->
                    if (room.name.isNullOrBlank()) room.roomId else room.name!!
                })
            }
            MatrixRoomListSortingElement.SERVER_NAME -> {
                if (sortingDirectionReq != null && sortingDirectionReq) it.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { room ->
                    publicAPIData.matrixServers.getValue(room.serverId).name
                })
                else it.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { room ->
                    publicAPIData.matrixServers.getValue(room.serverId).name
                })
            }
            else -> {
                if (sortingDirectionReq != null && sortingDirectionReq) it.sortedBy { room ->
                    room.numJoinedMembers
                }
                else it.sortedByDescending { room ->
                    room.numJoinedMembers
                }
            }
        }
    }.let {
        when(gaFilteringReq) {
            MatrixRoomGuestCanJoinFilter.CAN_JOIN -> it.filter { room ->
                room.guestCanJoin
            }
            MatrixRoomGuestCanJoinFilter.CAN_NOT_JOIN -> it.filterNot { room ->
                room.guestCanJoin
            }
            else -> it
        }
    }.let {
        when(wrFilteringReq) {
            MatrixRoomWorldReadableFilter.IS_WORLD_READABLE -> it.filter {room ->
                room.worldReadable
            }
            MatrixRoomWorldReadableFilter.IS_NOT_WORLD_READABLE -> it.filterNot {room ->
                room.worldReadable
            }
            else -> it
        }
    }.let {
        if (maxNOUFilteringReq != null)
            it.filter { room ->
                room.numJoinedMembers <= maxNOUFilteringReq
            }
        else
            it
    }.let {
        if (minNOUFilteringReq != null)
            it.filter {room ->
                room.numJoinedMembers >= minNOUFilteringReq
            }
        else
            it
    }


    val elmPerPageReqByCookie = req.cookie(MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_COOKIE_NAME)?.value?.toIntOrNull()
    val elmPerPageReqByQuery = getRootReqElmPerPageQuery(req).getOrNull().let {
        if (it != null && it > 0) it
        else null
    }
    val elmPerPage = when {
        elmPerPageReqByQuery != null -> elmPerPageReqByQuery
        elmPerPageReqByCookie != null -> elmPerPageReqByCookie
        else -> MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE
    }

    val maxPage = (filteredSortedMatrixRoomList.size / elmPerPage).let {
        if (filteredSortedMatrixRoomList.size % elmPerPage != 0) it + 1
        else it
    }

    val pageReq = getRootReqPageQuery(req).getOrDefault(MATRIX_ROOMS_PAGE_DEFAULT_PAGE).let {
        if (it != null && it > 0) {
            if (it < maxPage) it
            else maxPage
        }
        else MATRIX_ROOMS_PAGE_DEFAULT_PAGE
    }

    val slicedFilteredSortedMatrixRoomList = filteredSortedMatrixRoomList.slice(IntRange(
        elmPerPage*(pageReq-1),
        (elmPerPage*pageReq-1).let {
            if (it < filteredSortedMatrixRoomList.size-1) it else filteredSortedMatrixRoomList.size-1
        }
    ))


    val freeMarkerModelData = FreeMarkerDataModel (
        globalData = freeMarkerGlobalModelData,
        cssFileNames = listOf (
            PAGE_CSS_FILE_NAME
        ),
        jsFileNames = listOf (
            PAGE_NATIVE_JS_FILE_NAME,
            PAGE_JS_FILE_NAME
        ),
        texts = ResourceBundle.getBundle("pro.wsmi.roommap.client.backend.matrix_rooms_page.UITexts", freeMarkerGlobalModelData.texts.locale),
        queryParameters = QueryParameters (
            sorterReqName = MATRIX_ROOMS_PAGE_SORTER_REQ_NAME,
            sorterDirectionReqName = MATRIX_ROOMS_PAGE_SORTER_DIRECTION_REQ_NAME,
            gaFilterReqName = MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME,
            wrFilterReqName = MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME,
            serverFilterReqName = MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME,
            maxNOUFilterReqName = MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME,
            minNOUFilterReqName = MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME,
            roomsPerPageReqName = MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME,
            pageReqName = MATRIX_ROOMS_PAGE_PAGE_REQ_NAME,
            sorter = sortingReq?.toString(),
            sorterDirection = sortingDirectionReq,
            gaFilter = gaFilteringReq?.toString(),
            wrFilter = wrFilteringReq?.toString(),
            serverFilter = serverFilteringReq,
            maxNOUFilter = maxNOUFilteringReq,
            minNOUFilter = minNOUFilteringReq,
            roomsPerPage = elmPerPage,
            page = pageReq
        ),
        matrixRoomsData = FreeMarkerDataModel.MatrixRoomsData(
            pageMaxNum = maxPage,
            totalRoomsNum = filteredSortedMatrixRoomList.size,
            serverList = publicAPIData.matrixServers,
            roomList = slicedFilteredSortedMatrixRoomList
        )
    )

    val stringWriter = StringWriter()
    freemarkerTemplate.process(freeMarkerModelData, stringWriter)


    val responseElmPerPageCookie = if (elmPerPage == elmPerPageReqByQuery && elmPerPage != elmPerPageReqByCookie)
        Cookie(name = MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_COOKIE_NAME, value = elmPerPage.toString(), maxAge = 16329600L, path = "/")
    else null


    return Response(Status.OK)
        .body(stringWriter.toString())
        .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
        .let {
            if (responseElmPerPageCookie != null)
                it.cookie(responseElmPerPageCookie)
            else it
        }
}