package ru.livetyping.zarina.data.old.shop.remote.api

import ru.livetyping.zarina.data.old.shop.remote.api.dto.ReserveRequestBody
import ru.livetyping.zarina.data.old.shop.remote.api.dto.ShopCountryDto
import ru.livetyping.zarina.data.old.shop.remote.api.dto.StockDto

interface IZarinaShopApi {
    suspend fun getShops(): List<ShopCountryDto>
    suspend fun getStocks(offerBarcode: String, cityId: String): List<StockDto>
    suspend fun reserve(offerBarcode: String, shopId: String, body: ReserveRequestBody)
}
