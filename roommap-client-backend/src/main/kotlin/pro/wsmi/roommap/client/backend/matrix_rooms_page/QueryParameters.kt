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
    val serverFilter: String?,
    val maxNOUFilter: String?,
    val minNOUFilter: String?,
    val roomsPerPage: Int,
    val page: Int
)
{
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
            if(it.isNotEmpty()) "$it&" else {""} + "${this.serverFilterReqName}=${this.serverFilter}"
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

    fun copyWithNewPage(newPage: Int) : QueryParameters = this.copy(page = newPage)
    fun copyWithNewSorter(newSorter: String, newDirection: Boolean) : QueryParameters = this.copy(sorter = newSorter, sorterDirection = newDirection)
}