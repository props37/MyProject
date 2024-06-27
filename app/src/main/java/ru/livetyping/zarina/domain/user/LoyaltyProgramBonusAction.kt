package ru.livetyping.zarina.domain.user

import java.time.LocalDate

data class LoyaltyProgramBonusAction(
    val bonusCount: Int,
    val title: String,
    val type: Type,
    val date: LocalDate?,
    val expirationDate: LocalDate?,
) {
    @JvmInline
    value class Id(val value: String)

    enum class Type { EARNED, SPENT }
}
