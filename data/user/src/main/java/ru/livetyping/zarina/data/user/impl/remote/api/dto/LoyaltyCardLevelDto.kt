package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard

@Serializable
@JvmInline
internal value class LoyaltyCardLevelDto(val value: String) {
    fun toLoyaltyCardLevel(): LoyaltyCard.Level = when (value) {
        "PRIME" -> LoyaltyCard.Level.PRIME
        "PRIORITY" -> LoyaltyCard.Level.PRIORITY
        "STAR" -> LoyaltyCard.Level.STAR
        else -> error("Unknown loyalty card level $value")
    }
}
