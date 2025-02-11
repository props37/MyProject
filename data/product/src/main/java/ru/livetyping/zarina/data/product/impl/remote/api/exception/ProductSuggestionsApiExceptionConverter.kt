package ru.livetyping.zarina.data.product.impl.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.core.domain.model.product.exception.ProductNotFoundException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import javax.inject.Inject

internal class ProductSuggestionsApiExceptionConverter @Inject constructor() :
    KtorApiExceptionConverter() {

    override suspend fun convert(e: ClientRequestException): Nothing {
        when (e.response.status) {
            HttpStatusCode.NotFound -> throw ProductNotFoundException()
            else -> throw e
        }
    }
}
