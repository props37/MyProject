package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.user.LoyaltyCard

@Serializable
data class GetLoyaltyCardDto(
    @SerialName("has_card")
    val hasCard: Boolean? = null,

    @SerialName("card")
    val card: LoyaltyCardDto? = null,
) {
    fun toLoyaltyCard(): LoyaltyCard {
        checkNotNull(card) { "card is null" }
        return card.toLoyaltyCard()
    }
}
