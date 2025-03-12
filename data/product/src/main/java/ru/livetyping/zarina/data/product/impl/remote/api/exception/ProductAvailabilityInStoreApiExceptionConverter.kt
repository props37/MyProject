package ru.livetyping.zarina.data.product.impl.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.core.domain.model.product.exception.ProductNotAvailableException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import javax.inject.Inject

internal class ProductAvailabilityInStoreApiExceptionConverter @Inject constructor() :
    KtorApiExceptionConverter() {

    override suspend fun convert(e: ResponseException): Nothing {
        when (e.response.status) {
            HttpStatusCode.NotFound -> throw ProductNotAvailableException()
            else -> throw e
        }
    }
}
