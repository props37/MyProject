package ru.livetyping.zarina.feature.profile.ui.impl.bonushistory

internal sealed interface BonusHistoryScreenAction {
    data object BackClicked : BonusHistoryScreenAction
}
