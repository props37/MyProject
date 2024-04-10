package ru.livetyping.zarina.data.favorite.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.data.favorite.remote.api.dto.FavoriteProductIdsDto
import ru.livetyping.zarina.data.favorite.remote.api.dto.FavoriteProductsDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import javax.inject.Inject

class FavoriteApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getFavoriteProductIds(): FavoriteProductIdsDto {
        return httpClient.get("/api/v1/favorites-list").body()
    }

    suspend fun getFavoriteProducts(page: Int): FavoriteProductsDto {
        return httpClient.get("/api/v1/favorites") {
            parameter("page", page)
        }.body()
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        httpClient.post("/api/favorites/product/${productId.value}")
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        httpClient.get("/api/favorites/product/${productId.value}/remove")
    }

    suspend fun clearFavoriteProducts() {
        httpClient.delete("/api/v1/favorites")
    }
}
