package ru.zarina.zarina.data.old.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.old.Stock

@Serializable
data class StockDto(
    @SerialName("shop")
    val shop: ShopDto? = null,
    @SerialName("amount")
    val amount: StockAmountDto = StockAmountDto.LAST_CHANCE,
) {
    fun toDomain(): Stock? {
        val shop = shop?.toDomain()
        return if (
            ApiContract.isNotNull(shop, "shop")
            && ApiContract.isNotNull(amount, "amount")
        )
            return Stock(shop, amount.toDomain())
        else
            null
    }
}
