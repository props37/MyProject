package ru.zarina.zarina.data.shop

import ru.zarina.zarina.data.shop.remote.IShopRemoteSource
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remote: IShopRemoteSource,
) : IShopRepository {

    override suspend fun getShops() = remote.getShops()

}
