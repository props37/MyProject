package ru.zarina.zarina.data.shop

import ru.zarina.zarina.domain.shop.ShopCountry

interface IShopRepository {

    suspend fun getShops(): List<ShopCountry>

}
