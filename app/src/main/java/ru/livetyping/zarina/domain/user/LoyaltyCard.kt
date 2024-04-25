package ru.livetyping.zarina.domain.user

data class LoyaltyCard(
    val number: Number,
    val level: LoyaltyCardLevel,
    val nextLevel: NextLevel,
    val bonuses: Bonuses,
    val discountPercent: Int,
    val totalPurchaseSum: Int,
) {
    @JvmInline
    value class Number(val value: String)

    data class NextLevel(
        val level: LoyaltyCardLevel,
        val remainingPurchaseSum: Int,
    )

    data class Bonuses(
        val bonusCount: Int,
        val expectedBonusCount: Int,
    )
}
