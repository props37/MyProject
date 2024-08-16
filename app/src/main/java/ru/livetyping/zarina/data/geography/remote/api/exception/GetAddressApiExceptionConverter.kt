package ru.livetyping.zarina.data.geography.remote.api.exception

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.data.common.remote.api.exception.KtorApiExceptionConverter
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import javax.inject.Inject

class GetAddressApiExceptionConverter @Inject constructor(
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun handle(e: ClientRequestException): Nothing {
        if (e.response.status == HttpStatusCode.UnprocessableEntity) {
            throw EmptySearchQueryException()
        } else {
            throw e
        }
    }
}
