package ru.livetyping.zarina.data.old.shop.remote

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.shop.remote.api.IZarinaShopApi
import ru.livetyping.zarina.data.old.shop.remote.api.dto.ReserveRequestBody
import ru.livetyping.zarina.data.old.shop.remote.api.dto.toCountries
import ru.livetyping.zarina.data.old.shop.remote.api.dto.toShops
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Country
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Shop
import ru.livetyping.zarina.domain.old.Stock

@Factory
class ZarinaShopRemoteSource(
    private val api: IZarinaShopApi,
) : IShopRemoteSource {

    override suspend fun getCountries(): List<Country> {
        return api.getShops().toCountries()
    }

    override suspend fun getShops(city: City): List<Shop> {
        val shopsByCity = api.getShops().toShops()
        val key = shopsByCity.keys.firstOrNull { it.id == city.id }
        return shopsByCity[key].orEmpty()
    }

    override suspend fun getStocks(offer: Offer, city: City): List<Stock> {
        return api.getStocks(offer.barcode.value, city.id.id).mapNotNull { it.toDomain() }
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
        api.reserve(offer.barcode.value, shop.id, body)
    }

}
