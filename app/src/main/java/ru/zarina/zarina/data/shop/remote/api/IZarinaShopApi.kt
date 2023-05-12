package ru.zarina.zarina.data.shop.remote.api

import ru.zarina.zarina.data.shop.remote.api.dto.ShopCountryDto
import ru.zarina.zarina.data.shop.remote.api.dto.StockDto

interface IZarinaShopApi {
    suspend fun getShops(): List<ShopCountryDto>
    suspend fun getStocks(offerId: String, cityId: String): List<StockDto>
}
