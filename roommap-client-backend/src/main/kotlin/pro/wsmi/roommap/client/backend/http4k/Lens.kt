package pro.wsmi.roommap.client.backend.http4k

import org.http4k.core.Request
import org.http4k.lens.BiDiLens

fun <T> BiDiLens<Request, T?>.asResult() : (Request) -> Result<T?> = { req ->
    try {
        Result.success(this(req))
    } catch (e: Exception) {
        Result.failure(e)
    }
}