package ru.zarina.zarina.data.content

import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Url

interface IContentRepository {
    suspend fun getOnboardingSplash(): Url?
    suspend fun getBanners(): List<Banner>
}
