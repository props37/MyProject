package ru.livetyping.zarina.data.product.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.product.exception.ProductNotAvailableException
import javax.inject.Inject

class ProductAvailabilityInStoreApiExceptionConverter @Inject constructor() : KtorApiExceptionConverter() {
    override suspend fun handle(e: ResponseException): Nothing {
        when (e.response.status) {
            HttpStatusCode.NotFound, HttpStatusCode.InternalServerError -> {
                throw ProductNotAvailableException()
            }

            else -> throw e
        }
    }
}
