package pro.wsmi.roommap.client.backend.matrix_rooms_page

data class QueryParameters(
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
    val roomsPerPage: Int,
    val page: Int
)
{
    @Suppress("unused")
    fun toUrlFormat(): String = if (this.sorter != null) "${this.sorterReqName}=${this.sorter}" else {""}.let {
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
        if(it.isNotEmpty()) "$it&" else {""} + "${this.roomsPerPageReqName}=${this.roomsPerPage}&${this.pageReqName}=${this.page}"
    }

    @Suppress("unused")
    fun toOwnFormat (
        pattern: String,
        ignorePage: Boolean = false,
        ignoreRoomsPerPage: Boolean = false,
        ignoreNOUFilter: Boolean = false,
        ignoreGAFilter: Boolean = false,
        ignoreWRFilter: Boolean = false,
        ignoreServerFilter: Boolean = false
    ): String =
        if (this.sorter != null)
            pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.sorterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.sorter.toString())
        else {
            ""
        }.let {
            if (this.sorterDirection != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.sorterDirectionReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.sorterDirection.toString())
            else it
        }.let {
            if (!ignoreGAFilter && this.gaFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.gaFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.gaFilter.toString())
            else it
        }.let {
            if (!ignoreWRFilter && this.wrFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.wrFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.wrFilter.toString())
            else it
        }.let {
            if (!ignoreServerFilter && this.serverFilter != null)
            {
                it + this.serverFilter.joinToString(separator = "") { server ->
                    if (server.isNotEmpty())
                        "\n" + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.serverFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = server)
                    else ""
                }
            }
            else it
        }.let {
            if (!ignoreNOUFilter && this.maxNOUFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.maxNOUFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.maxNOUFilter.toString())
            else it
        }.let {
            if (!ignoreNOUFilter && this.minNOUFilter != null)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.minNOUFilterReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.minNOUFilter.toString())
            else it
        }.let {
            if (!ignorePage)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.pageReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.page.toString())
            else it
        }.let {
            if (!ignoreRoomsPerPage)
                if(it.isNotEmpty()) "$it\n" else {""} + pattern.replace(oldValue = PATTERN_NAME_TAG, newValue = this.roomsPerPageReqName).replace(oldValue = PATTERN_VALUE_TAG, newValue = this.roomsPerPage.toString())
            else it
        }

    @Suppress("unused")
    fun copyWithNewPage(newPage: Int) : QueryParameters = this.copy(page = newPage)
    @Suppress("unused")
    fun copyWithNewSorter(newSorter: String, newDirection: Boolean) : QueryParameters = this.copy(sorter = newSorter, sorterDirection = newDirection)

    companion object {
        const val PATTERN_NAME_TAG= "£{name}£"
        const val PATTERN_VALUE_TAG= "£{value}£"
    }
}