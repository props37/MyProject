package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model

internal enum class BonusHistoryTab {
    BONUS_HISTORY, EXPECTED_BONUSES;

    companion object {
        fun getAll(): List<BonusHistoryTab> = listOf(BONUS_HISTORY, EXPECTED_BONUSES)
    }
}
