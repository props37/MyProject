package ru.zarina.zarina.data.old.content.remote

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Selection
import ru.zarina.zarina.domain.Url

interface IContentRemoteSource {
    suspend fun getOnboardingSplash(): Url?
    suspend fun getBanners(): List<Banner>
    fun getSelections(): Flow<List<Selection>>
}
