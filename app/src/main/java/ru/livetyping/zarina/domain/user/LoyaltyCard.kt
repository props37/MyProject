package ru.livetyping.zarina.domain.user

data class LoyaltyCard(
    val number: Number,
    val currentLevel: LoyaltyCardLevel,
    val currentLevelName: String,
    val nextLevelInfo: NextLevelInfo?,
    val bonuses: Bonuses,
    val totalPurchaseSum: Int,
) {
    @JvmInline
    value class Number(val value: String)

    data class NextLevelInfo(
        val level: LoyaltyCardLevel,
        val levelName: String,
        val requiredPurchaseSum: Int,
    )

    data class Bonuses(
        val bonusCount: Int,
        val expectedBonusCount: Int,
    )
}
