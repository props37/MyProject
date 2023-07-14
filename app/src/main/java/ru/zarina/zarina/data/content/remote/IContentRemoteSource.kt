package ru.zarina.zarina.data.content.remote

import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Url

interface IContentRemoteSource {
    suspend fun getOnboardingSplash(): Url?
    suspend fun getBanners(): List<Banner>
}
