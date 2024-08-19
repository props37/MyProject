package ru.livetyping.zarina.data.geography.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.domain.geography.exception.AddressNotFoundException
import javax.inject.Inject

class GetAddressApiExceptionConverter @Inject constructor() : KtorApiExceptionConverter() {
    override suspend fun handle(e: ClientRequestException): Nothing {
        when (e.response.status) {
            HttpStatusCode.UnprocessableEntity -> throw EmptySearchQueryException()
            HttpStatusCode.NotFound -> throw AddressNotFoundException()
            else -> throw e
        }
    }
}
