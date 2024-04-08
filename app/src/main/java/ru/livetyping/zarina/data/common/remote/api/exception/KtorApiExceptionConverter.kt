package ru.livetyping.zarina.data.common.remote.api.exception

import io.ktor.client.plugins.ClientRequestException

abstract class KtorApiExceptionConverter {
    suspend operator fun <T> invoke(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: ClientRequestException) {
            handle(e)
        }
    }

    protected abstract suspend fun handle(e: ClientRequestException): Nothing
}
