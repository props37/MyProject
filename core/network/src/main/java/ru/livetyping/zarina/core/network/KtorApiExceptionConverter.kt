package ru.livetyping.zarina.core.network

import io.ktor.client.plugins.ResponseException

public abstract class KtorApiExceptionConverter {
    public suspend operator fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ResponseException) {
            convert(e)
        }
    }

    protected abstract suspend fun convert(e: ResponseException): Nothing
}
