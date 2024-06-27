package ru.livetyping.zarina.presentation.screen.loyaltyprogram

sealed class LoyaltyProgramScreenAction {
    data object ScreenClosed : LoyaltyProgramScreenAction()

    data object BonusHistoryClicked : LoyaltyProgramScreenAction()
}
