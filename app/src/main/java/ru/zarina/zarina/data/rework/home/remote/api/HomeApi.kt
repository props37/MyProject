package ru.zarina.zarina.data.rework.home.remote.api

import io.ktor.client.HttpClient
import ru.zarina.zarina.di.reworked.Qualifiers
import javax.inject.Inject

class HomeApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
)
