package ru.zarina.zarina.data.shop

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.domain.Stock

interface IShopRepository {

    suspend fun getCountries(): List<Country>

    suspend fun getShops(city: City): List<Shop>

    suspend fun getStocks(offer: Offer, city: City): List<Stock>

}
