package ru.zarina.zarina.data.rework.favorite.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun addProductToFavorites(productId: Product.Id) {
        httpClient.post("/api/favorites/product/${productId.value}")
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        httpClient.get("/api/favorites/product/${productId.value}/remove")
    }
}
