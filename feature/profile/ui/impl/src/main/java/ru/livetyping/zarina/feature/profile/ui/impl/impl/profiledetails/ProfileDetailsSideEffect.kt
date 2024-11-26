package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface ProfileDetailsSideEffect : SideEffect {
    data class Navigate(val action: ProfileDetailsScreenAction) : ProfileDetailsSideEffect
}
