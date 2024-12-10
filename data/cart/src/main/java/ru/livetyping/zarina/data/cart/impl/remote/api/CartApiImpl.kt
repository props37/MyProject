package ru.livetyping.zarina.data.cart.impl.remote.api

import io.ktor.client.HttpClient
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import javax.inject.Inject

internal class CartApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : CartApi {

}
