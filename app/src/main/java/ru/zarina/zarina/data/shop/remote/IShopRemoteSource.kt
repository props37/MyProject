package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.domain.shop.ShopCountry

interface IShopRemoteSource {
    suspend fun getShops(): List<ShopCountry>
}
