package ru.zarina.zarina.data.old.shop

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.shop.remote.IShopRemoteSource
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.Offer
import ru.zarina.zarina.domain.old.Shop

@Factory
class ShopRepository(
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
