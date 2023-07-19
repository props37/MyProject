package ru.zarina.zarina.data.favorites.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaFavoritesApi(
    @Named(Qualifiers.Authorization.TOKEN)
    private val client: HttpClient,
) : IZarinaFavoritesApi {
    override suspend fun add(productId: String) {
        client.post("favorites/product/$productId")
    }

    override suspend fun remove(productId: String) {
        client.delete("favorites/product/$productId")
    }
}