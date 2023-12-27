package ru.zarina.zarina.data.rework.home

import ru.zarina.zarina.data.rework.home.remote.HomeRemoteDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
)
