package ru.livetyping.zarina.presentation.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyCardLevel

val LoyaltyCardLevel.nameResId: Int
    get() = when (this) {
        LoyaltyCardLevel.PRIME -> R.string.loyalty_card_level_prime
        LoyaltyCardLevel.PRIORITY -> R.string.loyalty_card_level_priority
        LoyaltyCardLevel.STAR -> R.string.loyalty_card_level_star
    }
