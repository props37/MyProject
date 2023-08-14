package ru.zarina.zarina.data.shop.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.shop.remote.api.dto.ReserveRequestBody
import ru.zarina.zarina.data.shop.remote.api.dto.ShopCountryDto
import ru.zarina.zarina.data.shop.remote.api.dto.StockDto
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.exception.NotFoundException

@Factory
class KtorZarinaShopApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
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

    override suspend fun reserve(
        offerBarcode: String,
        shopId: String,
        body: ReserveRequestBody,
    ) {
        try {
            client.post("/api/products/stock/offers/$offerBarcode/shops/$shopId") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        } catch (exception: ClientRequestException) {
            if (exception.response.status == HttpStatusCode.NotFound)
                throw NotFoundException("Stocks for barcode $offerBarcode in shop $shopId are empty.")
            else
                throw exception
        }
    }

}
