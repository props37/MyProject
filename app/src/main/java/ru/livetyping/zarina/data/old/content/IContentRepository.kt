package ru.livetyping.zarina.data.old.content

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Banner
import ru.livetyping.zarina.domain.old.Selection
import ru.livetyping.zarina.domain.old.Url

interface IContentRepository {
    suspend fun getOnboardingSplash(): Url?
    suspend fun getBanners(): List<Banner>
    fun getSelections(): Flow<List<Selection>>
}
