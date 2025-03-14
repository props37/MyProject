package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import java.math.BigDecimal

@Serializable
internal data class LoyaltyCardDto(
    @SerialName("number")
    val number: String? = null,

    @SerialName("current_level")
    val currentLevel: LoyaltyCardLevelDto? = null,

    @SerialName("next_level")
    val nextLevel: LoyaltyCardLevelDto? = null,

    @SerialName("next_level_purchases_sum")
    val nextLevelPurchaseSum: Float? = null,

    @SerialName("balance")
    val balance: Int? = null,

    @SerialName("expected_bonuses")
    val expectedBonuses: Int? = null,

    @SerialName("bonus_percent")
    val bonusPercent: Int? = null,

    @SerialName("purchase_total")
    val purchaseTotal: Float? = null,
) {
    fun toLoyaltyCard(): LoyaltyCard {
        checkPropertyNotNull(number) { ::number }
        checkPropertyNotNull(currentLevel) { ::currentLevel }
        val nextLevelInfo = if (nextLevel != null && nextLevelPurchaseSum != null) {
            LoyaltyCard.NextLevelInfo(
                level = nextLevel.toLoyaltyCardLevel(),
                requiredPurchaseSum = BigDecimal(nextLevelPurchaseSum.toDouble()),
            )
        } else null
        val bonuses = LoyaltyCard.Bonuses(
            bonusCount = balance ?: 0,
            expectedBonusCount = expectedBonuses ?: 0,
        )
        return LoyaltyCard(
            number = LoyaltyCard.Number(number),
            level = currentLevel.toLoyaltyCardLevel(),
            nextLevelInfo = nextLevelInfo,
            bonuses = bonuses,
            totalPurchaseSum = purchaseTotal?.let { BigDecimal(it.toDouble()) } ?: BigDecimal.ZERO,
        )
    }
}
