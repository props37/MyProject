package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.data.shop.remote.api.IZarinaShopApi
import ru.zarina.zarina.domain.shop.ShopCountry
import javax.inject.Inject

class ZarinaShopRemoteSource @Inject constructor(
    private val api: IZarinaShopApi,
) : IShopRemoteSource {

    override suspend fun getShops(): List<ShopCountry> = api.getShops().toDomain()
}
