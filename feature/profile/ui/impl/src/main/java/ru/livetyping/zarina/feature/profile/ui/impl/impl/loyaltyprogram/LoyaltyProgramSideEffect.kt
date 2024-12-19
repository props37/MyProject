package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface LoyaltyProgramSideEffect : SideEffect {
    data class Navigate(val action: LoyaltyProgramScreenAction) : LoyaltyProgramSideEffect
}
