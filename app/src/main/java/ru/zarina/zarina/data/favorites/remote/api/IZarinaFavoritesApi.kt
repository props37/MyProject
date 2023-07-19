package ru.zarina.zarina.data.favorites.remote.api

interface IZarinaFavoritesApi {
    suspend fun add(productId: String)
    suspend fun remove(productId: String)
}