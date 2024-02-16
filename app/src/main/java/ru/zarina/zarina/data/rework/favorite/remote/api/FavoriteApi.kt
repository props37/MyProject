package ru.zarina.zarina.data.rework.favorite.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.zarina.zarina.data.rework.favorite.remote.api.dto.FavoriteProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
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
}
