package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.data.shop.remote.api.IZarinaShopApi
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Country
import ru.zarina.zarina.domain.Shop
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

}
