package ru.livetyping.zarina.data.wishlist.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductIdsDto
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductsDto
import javax.inject.Inject

internal class WishlistApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : WishlistApi {
    override suspend fun getWishlistProductIds(): WishlistProductIdsDto {
        return httpClient.get("/api/v1/favorites-list").body()
    }

    override suspend fun getWishlistProducts(page: Int): WishlistProductsDto {
        return httpClient.get("/api/v1/favorites") {
            parameter("page", page)
        }.body()
    }

    override suspend fun addProductToWishlist(productId: Product.Id) {
        httpClient.post("/api/favorites/product/${productId.value}")
    }

    override suspend fun removeProductFromWishlist(productId: Product.Id) {
        httpClient.get("/api/favorites/product/${productId.value}/remove")
    }

    override suspend fun clearWishlist() {
        httpClient.delete("/api/v1/favorites")
    }
}
