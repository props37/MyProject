package ru.zarina.zarina.data.rework.home

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.home.remote.HomeRemoteDataSource
import ru.zarina.zarina.domain.rework.home.HomeBanners
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
) {
    fun getBanners(): Flow<HomeBanners> {
        return remoteDataSource.getBanners()
    }
}
