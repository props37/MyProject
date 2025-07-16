package ru.livetyping.zarina.data.geography.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.core.domain.model.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.core.domain.model.geo.exception.AddressNotFoundException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import javax.inject.Inject

internal class AddressApiExceptionConverter @Inject constructor() : KtorApiExceptionConverter() {
    override suspend fun convert(e: ResponseException): Nothing {
        when (e.response.status) {
            HttpStatusCode.UnprocessableEntity -> throw EmptySearchQueryException()
            HttpStatusCode.NotFound -> throw AddressNotFoundException()
            else -> throw e
        }
    }
}
