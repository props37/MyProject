package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.data.shop.remote.api.IZarinaShopApi
import ru.zarina.zarina.data.shop.remote.api.dto.ReserveRequestBody
import ru.zarina.zarina.data.shop.remote.api.dto.toCountries
import ru.zarina.zarina.data.shop.remote.api.dto.toShops
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.domain.Stock
import javax.inject.Inject

class ZarinaShopRemoteSource @Inject constructor(
    private val api: IZarinaShopApi,
) : IShopRemoteSource {

    override suspend fun getCountries(): List<Country> {
        return api.getShops().toCountries()
    }

    override suspend fun getShops(city: City): List<Shop> {
        return api.getShops().toShops()[city].orEmpty()
    }

    override suspend fun getStocks(offer: Offer, city: City): List<Stock> {
        return api.getStocks(offer.barcode, city.id.id).mapNotNull { it.toDomain() }
    }

    override suspend fun reserve(
        offer: Offer,
        shop: Shop,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
    ) {
        val body = ReserveRequestBody(
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
        )
        api.reserve(offer.barcode, shop.id, body)
    }

}
