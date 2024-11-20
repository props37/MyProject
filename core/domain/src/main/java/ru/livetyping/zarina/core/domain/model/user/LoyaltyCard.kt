package ru.livetyping.zarina.core.domain.model.user

public data class LoyaltyCard(
    val number: Number,
    val level: Level,
    val nextLevelInfo: NextLevelInfo?,
    val bonuses: Bonuses,
    val totalPurchaseSum: Int,
) {
    @JvmInline
    public value class Number(public val value: String)

    public enum class Level {
        PRIME,
        PRIORITY,
        STAR,
    }

    public data class NextLevelInfo(
        val level: Level,
        val requiredPurchaseSum: Int,
    )

    public data class Bonuses(
        val bonusCount: Int,
        val expectedBonusCount: Int,
    )
}