package ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram

internal sealed interface LoyaltyProgramScreenAction {
    data object BackClicked : LoyaltyProgramScreenAction

    data object BonusHistoryClicked : LoyaltyProgramScreenAction
}
