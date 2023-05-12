package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Stock

@Serializable
data class StockDto(
    @SerialName("shop")
    val shop: ShopDto? = null,
    @SerialName("amount")
    val amount: String? = null,
) {
    fun toDomain(): Stock? {
        val shop = shop?.toDomain()
        return if (
            ApiContract.isNotNull(shop, "shop")
            && ApiContract.isNotNull(amount, "amount")
        )
            return Stock(shop, amount)
        else
            null
    }
}
