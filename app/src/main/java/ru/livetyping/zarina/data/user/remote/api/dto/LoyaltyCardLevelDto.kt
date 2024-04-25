package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.user.LoyaltyCardLevel

@Serializable
@JvmInline
value class LoyaltyCardLevelDto(val value: String) {
    fun toLoyaltyCardLevel(): LoyaltyCardLevel = when (value) {
        "PRIME" -> LoyaltyCardLevel.PRIME
        "PRIORITY" -> LoyaltyCardLevel.PRIORITY
        "STAR" -> LoyaltyCardLevel.STAR
        else -> error("Unknown loyalty card level $value")
    }
}
