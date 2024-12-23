package ru.livetyping.zarina.core.domain.model.user

import java.time.LocalDate

public data class LoyaltyProgramBonusAction(
    val bonusCount: Int,
    val title: String,
    val type: Type,
    val date: LocalDate?,
    val expirationDate: LocalDate?,
) {
    @JvmInline
    public value class Id(public val value: String)

    public enum class Type { EARNED, SPENT }
}
