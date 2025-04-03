package ru.livetyping.zarina.data.geography.impl.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.core.domain.model.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.core.domain.model.geo.exception.AddressNotFoundException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter

internal class AddressApiExceptionConverter : KtorApiExceptionConverter() {
    override suspend fun convert(e: ResponseException): Nothing {
        when (e.response.status) {
            HttpStatusCode.UnprocessableEntity -> throw EmptySearchQueryException()
            HttpStatusCode.NotFound -> throw AddressNotFoundException()
            else -> throw e
        }
    }
}
