package ru.livetyping.zarina.domain.user

enum class LoyaltyCardLevel {
    PRIME,
    PRIORITY,
    STAR,
}

val LoyaltyCardLevel.requiredPurchaseSum: Int
    get() = when (this) {
        LoyaltyCardLevel.PRIME -> 0
        LoyaltyCardLevel.PRIORITY -> REQUIRED_PURCHASE_SUM_PRIORITY
        LoyaltyCardLevel.STAR -> REQUIRED_PURCHASE_SUM_STAR
    }

operator fun LoyaltyCardLevel.contains(level: LoyaltyCardLevel): Boolean = when (this) {
    LoyaltyCardLevel.PRIME -> when (level) {
        LoyaltyCardLevel.PRIME -> true
        else -> false
    }

    LoyaltyCardLevel.PRIORITY -> when (level) {
        LoyaltyCardLevel.PRIME, LoyaltyCardLevel.PRIORITY -> true
        else -> false
    }
    LoyaltyCardLevel.STAR -> true
}

private const val REQUIRED_PURCHASE_SUM_PRIORITY = 10_000
private const val REQUIRED_PURCHASE_SUM_STAR = 30_000
