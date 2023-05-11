package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Shop

interface IShopRemoteSource {
    suspend fun getCountries(): List<Country>
    suspend fun getShops(city: City): List<Shop>
}
