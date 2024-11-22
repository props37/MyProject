package ru.livetyping.zarina.feature.profile.ui.impl.impl.util

import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.feature.profile.ui.impl.R

internal val LoyaltyCard.Level.nameResId: Int
    get() = when (this) {
        LoyaltyCard.Level.PRIME -> R.string.profile_loyalty_card_level_prime
        LoyaltyCard.Level.PRIORITY -> R.string.profile_loyalty_card_level_priority
        LoyaltyCard.Level.STAR -> R.string.profile_loyalty_card_level_star
    }
