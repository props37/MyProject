package ru.livetyping.zarina.feature.profile.ui.impl.bonushistory

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface BonusHistorySideEffect : SideEffect {
    data class Navigate(val action: BonusHistoryScreenAction) : BonusHistorySideEffect
}
