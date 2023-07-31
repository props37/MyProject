package ru.zarina.zarina.data.content

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Selection
import ru.zarina.zarina.domain.Url

interface IContentRepository {
    suspend fun getOnboardingSplash(): Url?
    suspend fun getBanners(): List<Banner>
    fun getSelections(): Flow<List<Selection>>
}
