package ru.zarina.zarina.data.shop.remote.api

import ru.zarina.zarina.data.shop.remote.api.dto.ShopsResponseDto

interface IZarinaShopApi {
    suspend fun getShops(): ShopsResponseDto
}
