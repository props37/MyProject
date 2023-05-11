package ru.zarina.zarina.data.shop

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Shop

interface IShopRepository {

    suspend fun getCountries(): List<Country>

    suspend fun getShops(city: City): List<Shop>

}
