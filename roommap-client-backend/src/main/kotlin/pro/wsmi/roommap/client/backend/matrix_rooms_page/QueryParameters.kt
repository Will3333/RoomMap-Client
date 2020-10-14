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

data class QueryParameters (
    val sorterReqName: String,
    val sorterDirectionReqName: String,
    val gaFilterReqName: String,
    val wrFilterReqName: String,
    val serverFilterReqName: String,
    val maxNOUFilterReqName: String,
    val minNOUFilterReqName: String,
    val roomsPerPageReqName: String,
    val pageReqName: String,
    val sorter: String?,
    val sorterDirection: Boolean?,
    val gaFilter: String?,
    val wrFilter: String?,
    val serverFilter: List<String>?,
    val maxNOUFilter: Int?,
    val minNOUFilter: Int?,
    val roomsPerPage: Int?,
    val page: Int?
) : pro.wsmi.roommap.client.backend.QueryParameters
{
    override fun toUrlFormat(): String = if (this.sorter != null) "${this.sorterReqName}=${this.sorter}" else {""}.let {
        if (this.sorterDirection != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.sorterDirectionReqName}=${this.sorterDirection}"
        else it
    }.let {
        if (this.gaFilter != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.gaFilterReqName}=${this.gaFilter}"
        else it
    }.let {
        if (this.wrFilter != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.wrFilterReqName}=${this.wrFilter}"
        else it
    }.let {
        if (this.serverFilter != null)
        {
            it + this.serverFilter.joinToString(separator = "") { server ->
                if (server.isNotEmpty())
                    "&${this.serverFilterReqName}=$server"
                else ""
            }
        }
        else it
    }.let {
        if (this.maxNOUFilter != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.maxNOUFilterReqName}=${this.maxNOUFilter}"
        else it
    }.let {
        if (this.minNOUFilter != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.minNOUFilterReqName}=${this.minNOUFilter}"
        else it
    }.let {
        if (this.roomsPerPage != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.roomsPerPageReqName}=${this.roomsPerPage}"
        else it
    }.let {
        if (this.page != null)
            if(it.isNotEmpty()) "$it&" else {""} + "${this.pageReqName}=${this.page}"
        else it
    }

    @Suppress("unused")
    override fun toOwnFormat (pattern: String): String =
        if (this.sorter != null)
            pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.sorterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.sorter.toString())
        else {
            ""
        }.let {
            if (this.sorterDirection != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.sorterDirectionReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.sorterDirection.toString())
            else it
        }.let {
            if (this.gaFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.gaFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.gaFilter.toString())
            else it
        }.let {
            if (this.wrFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.wrFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.wrFilter.toString())
            else it
        }.let {
            if (this.serverFilter != null)
            {
                it + this.serverFilter.joinToString(separator = "") { server ->
                    if (server.isNotEmpty())
                        "\n" + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.serverFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = server)
                    else ""
                }
            }
            else it
        }.let {
            if (this.maxNOUFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.maxNOUFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.maxNOUFilter.toString())
            else it
        }.let {
            if (this.minNOUFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.minNOUFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.minNOUFilter.toString())
            else it
        }.let {
            if (this.page != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.pageReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.page.toString())
            else it
        }.let {
            if (this.roomsPerPage != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.roomsPerPageReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.roomsPerPage.toString())
            else it
        }

    @Suppress("unused")
    fun copyWithNewPage(newPage: Int) : QueryParameters = this.copy(page = newPage)
    @Suppress("unused")
    fun copyWithNewSorter(newSorter: String, newDirection: Boolean) : QueryParameters = this.copy(sorter = newSorter, sorterDirection = newDirection)
    @Suppress("unused")
    fun copyDeleting (
        deletePage: Boolean = false,
        deleteRoomsPerPage: Boolean = false,
        deleteNOUFilter: Boolean = false,
        deleteGAFilter: Boolean = false,
        deleteWRFilter: Boolean = false,
        deleteServerFilter: Boolean = false
    ) : QueryParameters = this.let {
        if (deletePage)
            it.copy(page = null)
        else it
    }.let {
        if (deleteRoomsPerPage)
            it.copy(roomsPerPage = null)
        else it
    }.let {
        if (deleteNOUFilter)
            it.copy(maxNOUFilter = null, minNOUFilter = null)
        else it
    }.let {
        if (deleteGAFilter)
            it.copy(gaFilter = null)
        else it
    }.let {
        if (deleteWRFilter)
            it.copy(wrFilter = null)
        else it
    }.let {
        if (deleteServerFilter)
            it.copy(serverFilter = null)
        else it
    }

    companion object {
        const val PATTERN_NAME_TAG= "£{name}£"
        const val PATTERN_VALUE_TAG= "£{value}£"
    }
}