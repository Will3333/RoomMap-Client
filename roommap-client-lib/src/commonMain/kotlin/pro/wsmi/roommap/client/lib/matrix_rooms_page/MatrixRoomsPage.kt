/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.lib.matrix_rooms_page


const val MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_COOKIE_NAME = "room_per_page"
const val MATRIX_ROOMS_PAGE_ROOM_PER_PAGE_REQ_NAME = "matrix_rooms_per_page"
const val MATRIX_ROOMS_PAGE_PAGE_REQ_NAME = "page"
const val MATRIX_ROOMS_PAGE_SORTER_REQ_NAME = "sorter"
const val MATRIX_ROOMS_PAGE_SORTER_DIRECTION_REQ_NAME = "sorter_direction"
const val MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME = "ga_filter"
const val MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME = "wr_filter"
const val MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME = "server_filter"
const val MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME = "max_nou_filter"
const val MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME = "min_nou_filter"

const val MATRIX_ROOMS_PAGE_DEFAULT_PAGE = 1
const val MATRIX_ROOMS_PAGE_DEFAULT_ROOMS_PER_PAGE = 20


// Doesn't make these variables as const because Freemarker does not support it
object MatrixRoomsPageHTMLElmId {
    val NOU_FILTERS_DISPLAY_BUTTON = "matrix-rooms-nou-filters-display-button"
    val MAX_NOU_FILTER_CHECKBOX = "matrix-rooms-max-nou-filter-checkbox"
    val MAX_NOU_FILTER_TEXTFIELD = "matrix-rooms-max-nou-filter-textfield"
    val MIN_NOU_FILTER_CHECKBOX = "matrix-rooms-min-nou-filter-checkbox"
    val MIN_NOU_FILTER_TEXTFIELD = "matrix-rooms-min-nou-filter-textfield"
    val GA_TOOLTIP_CONTAINER= "matrix-rooms-ga-header-tooltip-container"
    val GA_TOOLTIP= "matrix-rooms-ga-header-tooltip"
    val GA_FILTER_DISPLAY_BUTTON = "matrix-rooms-ga-filter-display-button"
    val WR_TOOLTIP_CONTAINER= "matrix-rooms-wr-header-tooltip-container"
    val WR_TOOLTIP= "matrix-rooms-wr-header-tooltip"
    val WR_FILTER_DISPLAY_BUTTON = "matrix-rooms-wr-filter-display-button"
    val SERVER_FILTER_DISPLAY_BUTTON = "matrix-rooms-server-filter-display-button"
    val NOU_FILTERS_BLOCK = "matrix-rooms-nou-header-filters-block"
    val GA_FILTER_BLOCK = "matrix-rooms-ga-header-filter-block"
    val WR_FILTER_BLOCK = "matrix-rooms-wr-header-filter-block"
    val SERVER_FILTER_BLOCK = "matrix-rooms-server-header-filter-block"
}

// Doesn't make these variables as const because Freemarker does not support it
object MatrixRoomsPageCSSClasses {
    val NOU_FILTERS_BLOCK_DISPLAYED = "matrix-rooms-nou-header-filters-displayed"
    val NOU_FILTERS_BLOCK_HIDDEN = "matrix-rooms-nou-header-filters-hidden"
    val GA_FILTER_BLOCK_DISPLAYED = "matrix-rooms-ga-header-filter-displayed"
    val GA_FILTER_BLOCK_HIDDEN = "matrix-rooms-ga-header-filter-hidden"
    val WR_FILTER_BLOCK_DISPLAYED = "matrix-rooms-wr-header-filter-displayed"
    val WR_FILTER_BLOCK_HIDDEN = "matrix-rooms-wr-header-filter-hidden"
    val SERVER_FILTER_BLOCK_DISPLAYED = "matrix-rooms-server-header-filter-displayed"
    val SERVER_FILTER_BLOCK_HIDDEN = "matrix-rooms-server-header-filter-hidden"
    val MATRIX_ROOM_DETAILS_DISPLAYED = "matrix-room-details-displayed"
    val MATRIX_ROOM_DETAILS_HIDDEN = "matrix-room-details-hidden"
}