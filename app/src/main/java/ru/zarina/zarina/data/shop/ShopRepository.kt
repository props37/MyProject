package ru.zarina.zarina.data.shop

import ru.zarina.zarina.data.shop.remote.IShopRemoteSource
import ru.zarina.zarina.domain.shop.ShopCountry
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remote: IShopRemoteSource,
) : IShopRepository {

    override suspend fun getShops(): List<ShopCountry> {
        TODO("Not yet implemented")
    }

}
