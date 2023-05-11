package ru.zarina.zarina.data.shop

import ru.zarina.zarina.data.shop.remote.IShopRemoteSource
import ru.zarina.zarina.domain.City
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remote: IShopRemoteSource,
) : IShopRepository {

    override suspend fun getCountries() = remote.getCountries()

    override suspend fun getShops(city: City) = remote.getShops(city)

}
