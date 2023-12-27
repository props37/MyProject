package ru.zarina.zarina.data.rework.home.remote

import ru.zarina.zarina.data.rework.home.remote.api.HomeApi
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val api: HomeApi,
)
