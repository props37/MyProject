package ru.zarina.zarina.data.old.shop.remote

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.domain.Stock

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
