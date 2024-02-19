package ru.zarina.zarina.data.rework.cart.remote.api

import io.ktor.client.HttpClient
import ru.zarina.zarina.di.rework.Qualifiers
import javax.inject.Inject

class CartApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
)
