package ru.livetyping.zarina.data.common.remote.api.exception

interface ApiExceptionConverter {
    suspend operator fun <T> invoke(block: suspend () -> T): T
}
