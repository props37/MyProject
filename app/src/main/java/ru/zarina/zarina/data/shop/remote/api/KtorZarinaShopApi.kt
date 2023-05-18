package ru.zarina.zarina.data.shop.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import ru.zarina.zarina.data.shop.remote.api.dto.ShopCountryDto
import ru.zarina.zarina.data.shop.remote.api.dto.StockDto
import ru.zarina.zarina.di.Authorization
import ru.zarina.zarina.domain.exception.NotFoundException
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

    override suspend fun getStocks(offerBarcode: String, cityId: String): List<StockDto> {
        try {
            val response = client.get("/api/products/stock/offers/$offerBarcode/city/$cityId")
            val body = response.body<List<StockDto>?>()
            return body.orEmpty()
        } catch (exception: ClientRequestException) {
            if (exception.response.status == HttpStatusCode.NotFound)
                throw NotFoundException("Stocks for barcode $offerBarcode in city $cityId are empty.")
            else
                throw exception
        }
    }

}
