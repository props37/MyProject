package ru.livetyping.zarina.data.product.impl.remote.api.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidFirstNameException
import ru.livetyping.zarina.core.network.KtorApiExceptionConverter
import ru.livetyping.zarina.core.network.di.NetworkJson
import ru.livetyping.zarina.data.product.impl.remote.api.dto.SubscribeToProductEmailErrorDto
import ru.livetyping.zarina.data.product.impl.remote.api.dto.SubscribeToProductErrorDtoSerializer
import ru.livetyping.zarina.data.product.impl.remote.api.dto.SubscribeToProductFirstNameErrorDto
import javax.inject.Inject

internal class SubscribeToProductApiExceptionConverter @Inject constructor(
    @NetworkJson
    private val json: Json,
) : KtorApiExceptionConverter() {

    override suspend fun convert(e: ResponseException): Nothing {
        val responseText = e.response.bodyAsText()
        val errorDto =
            json.decodeFromString(SubscribeToProductErrorDtoSerializer(), responseText)
        when (errorDto) {
            is SubscribeToProductEmailErrorDto -> throw InvalidEmailException()
            is SubscribeToProductFirstNameErrorDto -> throw InvalidFirstNameException()
        }
    }
}
