package ru.zarina.zarina.data.favorites.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.favorites.remote.api.dto.FavoritesPageDto
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaFavoritesApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaFavoritesApi {
    override suspend fun add(productId: String) {
        client.post("/api/favorites/product/$productId")
    }

    override suspend fun remove(productId: String) {
        client.delete("/api/favorites/product/$productId")
    }

    override suspend fun getFavoritesPage(pageIndex: Int): FavoritesPageDto {
        val response = client.get("/api/favorites") {
            parameter("page", pageIndex)
        }
        return response.body()
    }
}
