package ru.livetyping.zarina.core.network

import io.ktor.client.plugins.ClientRequestException

public abstract class KtorApiExceptionConverter {
    public suspend operator fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            convert(e)
        }
    }

    protected abstract suspend fun convert(e: ClientRequestException): Nothing
}
