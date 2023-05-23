package ru.zarina.zarina.data.shop

import ru.zarina.zarina.data.shop.remote.IShopRemoteSource
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Shop
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remote: IShopRemoteSource,
) : IShopRepository {

    override suspend fun getCountries() = remote.getCountries()

    override suspend fun getShops(city: City) = remote.getShops(city)

    override suspend fun getStocks(offer: Offer, city: City) = remote.getStocks(offer, city)

    override suspend fun reserve(
        offer: Offer,
        shop: Shop,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
    ) = remote.reserve(offer, shop, firstName, lastName, email, phone)

}
