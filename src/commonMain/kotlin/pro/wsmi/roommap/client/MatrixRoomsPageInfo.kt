package pro.wsmi.roommap.client

data class MatrixRoomsPageInfo (
    val current_page: Int,
    val max_page: Int,
    val matrix_rooms_per_page: Int,
    val rooms_per_page_standard_num: List<Int>
)