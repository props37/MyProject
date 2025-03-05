package ru.livetyping.zarina.data.common.remote.api.exception

import io.ktor.client.plugins.ResponseException

abstract class KtorApiExceptionConverter {
    suspend operator fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ResponseException) {
            handle(e)
        }
    }

    protected abstract suspend fun handle(e: ResponseException): Nothing
}
