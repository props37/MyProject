package ru.livetyping.zarina.data.old.favorites.remote.api

import ru.livetyping.zarina.data.old.favorites.remote.api.dto.FavoritesPageDto

interface IZarinaFavoritesApi {
    suspend fun add(productId: String)
    suspend fun remove(productId: String)
    suspend fun getFavoritesPage(pageIndex: Int): FavoritesPageDto
}
