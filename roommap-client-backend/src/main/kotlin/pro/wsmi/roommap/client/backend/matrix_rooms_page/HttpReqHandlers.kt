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
private val rootReqMaxNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME)
private val rootReqMinNOUFilterQuery = Query.int().optional(MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME)
private val rootReqGAFilterQuery = Query.enum<MatrixRoomGuestCanJoinFilter>().optional(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME)
private val rootReqWRFilterQuery = Query.enum<MatrixRoomWorldReadableFilter>().optional(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME)
private val rootReqServerFilterQueries = Query.multi.optional(MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME)
private val rootReqPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_PAGE_REQ_NAME)
private val rootReqElmPerPageQuery = Query.int().optional(MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME)

@ExperimentalSerializationApi
fun handleMatrixRoomsPageReq(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerCfg: Configuration, mainPageTemplateFile: File, matrixServerFullList: MutableMap<String, MatrixServer>, matrixRoomFullList: MutableList<MatrixRoom>) : (Request) -> Response = { req ->

    val initialMatrixServerList = matrixServerFullList.toMap()
    val initialMatrixRoomList = matrixRoomFullList.toList()


    val sortingReq = rootReqSorterQuery(req)
    val sortingDirectionReq = rootReqSorterDirectionQuery(req)

    val sortedMatrixRoomList = when(sortingReq)
    {
        MatrixRoomListSortingElement.ROOM_NAME -> {
            if (sortingDirectionReq != null && sortingDirectionReq) initialMatrixRoomList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { room ->
                if (room.name.isNullOrBlank()) room.roomId else room.name!!
            })
            else initialMatrixRoomList.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { room ->
                if (room.name.isNullOrBlank()) room.roomId else room.name!!
            })
        }
        MatrixRoomListSortingElement.SERVER_NAME -> {
            if (sortingDirectionReq != null && sortingDirectionReq) initialMatrixRoomList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { room ->
                initialMatrixServerList.getValue(room.serverId).name
            })
            else initialMatrixRoomList.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { room ->
                initialMatrixServerList.getValue(room.serverId).name
            })
        }
        else -> {
            if (sortingDirectionReq != null && sortingDirectionReq) initialMatrixRoomList.sortedBy { room ->
                room.numJoinedMembers
            }
            else initialMatrixRoomList.sortedByDescending { room ->
                room.numJoinedMembers
            }
        }
    }


    val gaFilteringReq = rootReqGAFilterQuery(req)
    val wrFilteringReq = rootReqWRFilterQuery(req)
    val serverFilteringReq = rootReqServerFilterQueries(req)
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
        "page_info" to mapOf(
            "max_page" to maxPage,
            "max_page_length" to maxPage.toString().length,
            "matrix_rooms_total_num" to filteredSortedMatrixRoomList.size
        ),
        "serverList" to initialMatrixServerList.mapValues { server ->
            mapOf("name" to server.value.name, "apiUrl" to server.value.apiURL.toString(), "updateFreq" to server.value.updateFreq)
        },
        "roomList" to slicedFilteredSortedMatrixRoomList,
        "query_parameters" to QueryParameters(
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
        )
    )

    val mainPageTemplate = freemarkerCfg.getTemplate(mainPageTemplateFile.name)
    val stringWriter = StringWriter()
    mainPageTemplate.process(freemarkerModel, stringWriter)

    Response(Status.OK)
        .body(stringWriter.toString())
        .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
}