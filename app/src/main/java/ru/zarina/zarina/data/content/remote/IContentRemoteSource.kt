package ru.zarina.zarina.data.content.remote

import ru.zarina.zarina.domain.Url

interface IContentRemoteSource {
    suspend fun getOnboardingSplash(): Url?
}
