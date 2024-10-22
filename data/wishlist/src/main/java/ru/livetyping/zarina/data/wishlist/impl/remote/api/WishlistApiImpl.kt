package ru.livetyping.zarina.data.wishlist.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductIdsDto
import javax.inject.Inject

internal class WishlistApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : WishlistApi {
    override suspend fun getWishlistProductIds(): WishlistProductIdsDto {
        return httpClient.get("/api/v1/favorites-list").body()
    }
}
