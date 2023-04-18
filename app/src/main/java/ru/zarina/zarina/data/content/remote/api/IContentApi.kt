package ru.zarina.zarina.data.content.remote.api

import ru.zarina.zarina.data.content.remote.api.dto.SplashDto

interface IContentApi {
    suspend fun getOnboardingSplash(): SplashDto
}
