package pro.wsmi.roommap.client.backend.matrix_rooms_page

import freemarker.template.Configuration
import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.*
import org.http4k.lens.*
import pro.wsmi.roommap.client.backend.config.ClientConfiguration
import pro.wsmi.roommap.client.matrix_rooms_page.*
import pro.wsmi.roommap.lib.api.*
import java.io.File
import java.io.StringWriter

private val rootReqSorterQuery = Query.enum<MatrixRoomListSortingElement>().optional(MATRIX_ROOMS_PAGE_SORTER_REQ_NAME)
private val rootReqSorterDirectionQuery = Query.boolean().optional(MATRIX_ROOMS_PAGE_SORTER_DIRECTION_REQ_NAME)
private val rootReqGAFilterQuery = Query.enum<MatrixRoomGuestCanJoinFilter>().optional(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME)
private val rootReqWRFilterQuery = Query.enum<MatrixRoomWorldReadableFilter>().optional(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME)
private val rootReqServerFilterQuery = Query.string().optional(MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME)
private val rootReqMaxNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME)
private val rootReqMinNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME)
private val rootReqPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_PAGE_REQ_NAME)
private val rootReqElmPerPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME)

@ExperimentalSerializationApi
fun handleMatrixRoomsPageReq(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerCfg: Configuration, mainPageTemplateFile: File, matrixServerFullList: MutableMap<String, MatrixServer>, matrixRoomFullList: MutableList<MatrixRoom>) : (Request) -> Response = { req ->

    val initialMatrixServerList = matrixServerFullList.toMap()
    val initialMatrixRoomList = matrixRoomFullList.toList()


    val sortingReq = rootReqSorterQuery(req) ?: MatrixRoomListSortingElement.NUM_JOINED_MEMBERS
    val sortingDirectionReq = rootReqSorterDirectionQuery(req) ?: false

    val sortedMatrixRoomList = when(sortingReq)
    {
        MatrixRoomListSortingElement.ROOM_NAME -> {
            if (sortingDirectionReq) initialMatrixRoomList.sortedBy { room ->
                room.name ?: room.roomId
            }
            else initialMatrixRoomList.sortedByDescending { room ->
                room.name ?: room.roomId
            }
        }
        MatrixRoomListSortingElement.SERVER_NAME -> {
            if (sortingDirectionReq) initialMatrixRoomList.sortedBy { room ->
                initialMatrixServerList[room.serverId]?.name
            }
            else initialMatrixRoomList.sortedByDescending { room ->
                initialMatrixServerList[room.serverId]?.name
            }
        }
        else -> {
            if (sortingDirectionReq) initialMatrixRoomList.sortedBy { room ->
                room.numJoinedMembers
            }
            else initialMatrixRoomList.sortedByDescending { room ->
                room.numJoinedMembers
            }
        }
    }


    val gaFilteringReq = rootReqGAFilterQuery(req) ?: MatrixRoomGuestCanJoinFilter.NO_FILTER
    val wrFilteringReq = rootReqWRFilterQuery(req) ?: MatrixRoomWorldReadableFilter.NO_FILTER
    val serverFilteringReq = rootReqServerFilterQuery(req)?.split("+", ignoreCase = true)
    val maxNOUFilteringReq = rootReqMaxNOUFilterQuery(req)
    val minNOUFilteringReq = rootReqMinNOUFilterQuery(req)

    val filteredSortedMatrixRoomList = sortedMatrixRoomList.let {
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
        if (serverFilteringReq != null)
        {
            val newList = mutableListOf<MatrixRoom>()
            serverFilteringReq.forEach { serverName ->
                newList.addAll(it.filter { room ->
                    initialMatrixServerList[room.serverId]?.name == serverName
                })
            }
            newList.toList()
        }
        else
            it
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


    val elmPerPage = rootReqElmPerPageQuery(req).let {
        if (it != null && it > 0) it
        else MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE
    }

    val maxPage = (filteredSortedMatrixRoomList.size / elmPerPage).let {
        if ((filteredSortedMatrixRoomList.size % elmPerPage) != 0) it + 1
        else it
    }

    val pageReq = rootReqPageQuery(req).let {
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


    val freemarkerModel = mapOf(
        "debug_mode" to debugMode,
        "website_info" to mapOf("name" to clientCfg.websiteName),
        "queries" to mapOf(
            "sorter_req_name" to MATRIX_ROOMS_PAGE_SORTER_REQ_NAME,
            "sorter_direction_req_name" to MATRIX_ROOMS_PAGE_SORTER_DIRECTION_REQ_NAME,
            "ga_filter_req_name" to MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME,
            "wr_filter_req_name" to MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME,
            "server_filter_req_name" to MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME,
            "max_nou_filter_req_name" to MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME,
            "min_nou_filter_req_name" to MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME,
            "rooms_per_page_req_name" to MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME,
            "page_req_name" to MATRIX_ROOMS_PAGE_PAGE_REQ_NAME
        ),
        "page_info" to mapOf(
            "current_page_number" to pageReq,
            "max_page" to maxPage,
            "max_page_length" to maxPage.toString().length,
            "matrix_rooms_total_num" to filteredSortedMatrixRoomList.size,
            "matrix_rooms_per_page" to elmPerPage,
            "sorter" to sortingReq,
            "sorter_direction" to sortingDirectionReq,
            "ga_filter" to gaFilteringReq,
            "wr_filter" to wrFilteringReq,
            "server_filter" to serverFilteringReq,
            "max_nou_filter" to maxNOUFilteringReq,
            "min_nou_filter" to minNOUFilteringReq
        ),
        "serverList" to initialMatrixServerList.mapValues { server ->
            mapOf("name" to server.value.name, "apiUrl" to server.value.apiURL.toString(), "updateFreq" to server.value.updateFreq)
        },
        "roomList" to slicedFilteredSortedMatrixRoomList
    )

    val mainPageTemplate = freemarkerCfg.getTemplate(mainPageTemplateFile.name)
    val stringWriter = StringWriter()
    mainPageTemplate.process(freemarkerModel, stringWriter)

    Response(Status.OK)
        .body(stringWriter.toString())
        .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
}