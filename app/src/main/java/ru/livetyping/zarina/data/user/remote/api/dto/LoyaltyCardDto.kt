package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.user.LoyaltyCard

@Serializable
data class LoyaltyCardDto(
    @SerialName("number")
    val number: String? = null,

    @SerialName("current_level")
    val currentLevel: LoyaltyCardLevelDto? = null,

    @SerialName("current_level_description")
    val currentLevelDescription: String? = null,

    @SerialName("next_level")
    val nextLevel: LoyaltyCardLevelDto? = null,

    @SerialName("next_level_description")
    val nextLevelDescription: String? = null,

    @SerialName("next_level_purchases_sum")
    val nextLevelRequiredPurchaseSum: Int? = null,

    @SerialName("balance")
    val bonusCount: Int? = null,

    @SerialName("expected_bonuses")
    val expectedBonusCount: Int? = null,

    @SerialName("bonus_percent")
    val discountPercent: Int? = null,

    @SerialName("purchase_total")
    val totalPurchaseSum: Int? = null,
) {
    fun toLoyaltyCard(): LoyaltyCard {
        checkNotNull(number) { "number is null" }
        checkNotNull(currentLevel) { "currentLevel is null" }
        checkNotNull(currentLevelDescription) { "currentLevelDescription is null" }
        val nextLevelInfo =
            if (nextLevel != null && nextLevelDescription != null && nextLevelRequiredPurchaseSum != null) {
                LoyaltyCard.NextLevelInfo(
                    level = nextLevel.toLoyaltyCardLevel(),
                    levelName = nextLevelDescription,
                    requiredPurchaseSum = nextLevelRequiredPurchaseSum,
                )
            } else null
        val bonuses = LoyaltyCard.Bonuses(
            bonusCount = bonusCount ?: 0,
            expectedBonusCount = expectedBonusCount ?: 0,
        )
        return LoyaltyCard(
            number = LoyaltyCard.Number(number),
            currentLevel = currentLevel.toLoyaltyCardLevel(),
            currentLevelName = currentLevelDescription,
            nextLevelInfo = nextLevelInfo,
            bonuses = bonuses,
            totalPurchaseSum = totalPurchaseSum ?: 0,
        )
    }
}
