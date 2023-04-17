package ru.zarina.zarina.data.content.remote.api

import ru.zarina.zarina.data.content.remote.api.dto.SplashDto
import javax.inject.Inject

class ContentApi @Inject constructor() : IContentApi {
    override suspend fun getOnboardingSplash(): SplashDto {
        TODO("Not yet implemented")
    }

}
