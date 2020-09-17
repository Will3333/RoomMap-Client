package pro.wsmi.roommap.client

enum class MatrixRoomPerPage(val number: Int)
{
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    THOUSAND(1000);

    operator fun plus(other: Int) : Int = this.number + other
    operator fun minus(other: Int) : Int = this.number - other
    operator fun times(other: Int) : Int = this.number * other
    operator fun div(other: Int) : Int = this.number / other
    operator fun rem(other: Int) : Int = this.number % other
    operator fun rangeTo(other: Int) : IntRange = this.number..other
}