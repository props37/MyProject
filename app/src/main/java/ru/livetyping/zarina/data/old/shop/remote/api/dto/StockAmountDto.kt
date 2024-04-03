package ru.livetyping.zarina.data.old.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.old.Stock

@Serializable
enum class StockAmountDto {
    @SerialName("last_chance")
    LAST_CHANCE,

    @SerialName("little")
    LITTLE,

    @SerialName("enough")
    ENOUGH,

    @SerialName("a_lot")
    A_LOT;

    fun toDomain() = when (this) {
        LAST_CHANCE -> Stock.Amount.ONE
        LITTLE -> Stock.Amount.FEW
        ENOUGH -> Stock.Amount.SOME
        A_LOT -> Stock.Amount.MANY
    }
}
