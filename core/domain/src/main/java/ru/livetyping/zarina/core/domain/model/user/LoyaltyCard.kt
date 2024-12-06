package ru.livetyping.zarina.core.domain.model.user

// Marked as stable on config/compose/stability_config.txt
public data class LoyaltyCard(
    val number: Number,
    val level: Level,
    val nextLevelInfo: NextLevelInfo?,
    val bonuses: Bonuses,
    val totalPurchaseSum: Int,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Number(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    public enum class Level {
        PRIME,
        PRIORITY,
        STAR,
    }

    // Marked as stable on config/compose/stability_config.txt
    public data class NextLevelInfo(
        val level: Level,
        val requiredPurchaseSum: Int,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class Bonuses(
        val bonusCount: Int,
        val expectedBonusCount: Int,
    )
}

public val LoyaltyCard.Level.requiredPurchaseSum: Int
    get() = when (this) {
        LoyaltyCard.Level.PRIME -> 0
        LoyaltyCard.Level.PRIORITY -> REQUIRED_PURCHASE_SUM_PRIORITY
        LoyaltyCard.Level.STAR -> REQUIRED_PURCHASE_SUM_STAR
    }

public operator fun LoyaltyCard.Level.contains(level: LoyaltyCard.Level): Boolean = when (this) {
    LoyaltyCard.Level.PRIME -> when (level) {
        LoyaltyCard.Level.PRIME -> true
        else -> false
    }

    LoyaltyCard.Level.PRIORITY -> when (level) {
        LoyaltyCard.Level.PRIME, LoyaltyCard.Level.PRIORITY -> true
        else -> false
    }
    LoyaltyCard.Level.STAR -> true
}

private const val REQUIRED_PURCHASE_SUM_PRIORITY = 10_000
private const val REQUIRED_PURCHASE_SUM_STAR = 30_000
