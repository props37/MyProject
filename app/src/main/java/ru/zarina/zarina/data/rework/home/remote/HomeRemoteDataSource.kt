package ru.zarina.zarina.data.rework.home.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.home.remote.api.HomeApi
import ru.zarina.zarina.domain.rework.home.HomeBanners
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: HomeApi,
) {
    fun getBanners(): Flow<HomeBanners> = flow {
        val homeBanners = api.getBanners().toHomeBanners()
        emit(homeBanners)
    }
}
