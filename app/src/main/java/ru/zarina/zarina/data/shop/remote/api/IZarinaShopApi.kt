package ru.zarina.zarina.data.shop.remote.api

import ru.zarina.zarina.data.shop.remote.api.dto.ShopCountryDto

interface IZarinaShopApi {
    suspend fun getShops(): List<ShopCountryDto>
}
