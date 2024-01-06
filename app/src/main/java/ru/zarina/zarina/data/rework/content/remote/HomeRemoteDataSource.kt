package ru.zarina.zarina.data.rework.content.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.content.remote.api.HomeApi
import ru.zarina.zarina.domain.rework.home.HomeBanners
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: HomeApi,
) {
    fun getHomeBanners(): Flow<HomeBanners> = flow {
        val homeBanners = api.getHomeBanners().toHomeBanners()
        emit(homeBanners)
    }
}
