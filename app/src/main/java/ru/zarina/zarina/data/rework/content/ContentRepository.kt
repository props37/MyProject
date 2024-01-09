package ru.zarina.zarina.data.rework.content

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.content.remote.ContentRemoteDataSource
import ru.zarina.zarina.domain.rework.content.HomeBanners
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val remoteDataSource: ContentRemoteDataSource,
) {
    fun getHomeBanners(): Flow<HomeBanners> {
        return remoteDataSource.getHomeBanners()
    }
}
