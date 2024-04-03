package ru.livetyping.zarina.data.old.shop.remote

import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Country
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Shop
import ru.livetyping.zarina.domain.old.Stock

interface IShopRemoteSource {

    suspend fun getCountries(): List<Country>

    suspend fun getShops(city: City): List<Shop>

    suspend fun getStocks(offer: Offer, city: City): List<Stock>

    suspend fun reserve(
        offer: Offer,
        shop: Shop,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
    )

}
