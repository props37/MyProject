package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory

internal sealed interface BonusHistoryScreenAction {
    data object BackClicked : BonusHistoryScreenAction
}
