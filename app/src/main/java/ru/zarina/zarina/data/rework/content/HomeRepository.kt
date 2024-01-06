package ru.zarina.zarina.data.rework.content

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.content.remote.HomeRemoteDataSource
import ru.zarina.zarina.domain.rework.home.HomeBanners
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
) {
    fun getHomeBanners(): Flow<HomeBanners> {
        return remoteDataSource.getHomeBanners()
    }
}
