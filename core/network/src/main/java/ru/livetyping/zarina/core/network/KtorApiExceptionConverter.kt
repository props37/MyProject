package ru.livetyping.zarina.core.network

import io.ktor.client.plugins.ClientRequestException

public abstract class KtorApiExceptionConverter {
    public suspend operator fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            handle(e)
        }
    }

    protected abstract suspend fun handle(e: ClientRequestException): Nothing
}
