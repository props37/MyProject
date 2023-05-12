package ru.zarina.zarina.data.shop.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.shop.remote.api.dto.ShopCountryDto
import ru.zarina.zarina.data.shop.remote.api.dto.StockDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaShopApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaShopApi {

    override suspend fun getShops(): List<ShopCountryDto> {
        val response = client.get("/api/shops")
        val body = response.body<List<ShopCountryDto>?>()
        return body.orEmpty()
    }

    override suspend fun getStocks(offerId: String, cityId: String): List<StockDto> {
        val response = client.get("/api/stock/offers/$offerId/city/$cityId")
        val body = response.body<List<StockDto>?>()
        return body.orEmpty()
    }

}
