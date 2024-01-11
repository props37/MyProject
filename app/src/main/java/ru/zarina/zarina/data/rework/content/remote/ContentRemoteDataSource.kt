package ru.zarina.zarina.data.rework.content.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.content.remote.api.ContentApi
import ru.zarina.zarina.domain.rework.content.HomeContent
import javax.inject.Inject

class ContentRemoteDataSource @Inject constructor(
    private val api: ContentApi,
) {
    fun getHomeContent(): Flow<HomeContent> = flow {
        val homeBanners = api.getHomeBanners().toHomeContent()
        emit(homeBanners)
    }
}
