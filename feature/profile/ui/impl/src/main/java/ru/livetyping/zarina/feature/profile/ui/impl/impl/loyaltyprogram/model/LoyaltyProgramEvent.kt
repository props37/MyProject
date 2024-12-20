package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model

internal sealed interface LoyaltyProgramEvent {
    data object BackClicked : LoyaltyProgramEvent

    data object BonusHistoryClicked : LoyaltyProgramEvent

    data object ErrorRefreshClicked : LoyaltyProgramEvent
}
