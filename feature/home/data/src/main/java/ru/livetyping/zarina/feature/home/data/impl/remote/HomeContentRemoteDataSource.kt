package ru.livetyping.zarina.feature.home.data.impl.remote

import ru.livetyping.zarina.feature.home.data.repository.HomeContentRepositoryImpl
import javax.inject.Inject

internal class HomeContentRemoteDataSource @Inject constructor(
    private val api: HomeContentRepositoryImpl,
)
