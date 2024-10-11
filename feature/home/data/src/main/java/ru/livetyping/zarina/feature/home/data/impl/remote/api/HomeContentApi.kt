package ru.livetyping.zarina.feature.home.data.impl.remote.api

import io.ktor.client.HttpClient
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiQualifier
import javax.inject.Inject

internal class HomeContentApi @Inject constructor(
    @ZarinaApiQualifier(ZarinaApi.AUTHORIZED)
    private val httpClient: HttpClient,
)
