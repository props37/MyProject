package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class GetLoyaltyCardDto(
    @SerialName("has_card")
    val hasCard: Boolean? = null,

    @SerialName("card")
    val card: LoyaltyCardDto? = null,
) {
    fun toLoyaltyCard(): LoyaltyCard {
        checkPropertyNotNull(card) { ::card }
        return card.toLoyaltyCard()
    }
}
