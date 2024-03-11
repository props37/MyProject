package ru.zarina.zarina.data.old.shop

import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.Country
import ru.zarina.zarina.domain.old.Offer
import ru.zarina.zarina.domain.old.Shop
import ru.zarina.zarina.domain.old.Stock

interface IShopRepository {

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
