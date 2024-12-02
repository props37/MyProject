package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ProfileDetailsSideEffect : SideEffect {
    data class Navigate(val action: ProfileDetailsScreenAction) : ProfileDetailsSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ProfileDetailsSideEffect
}
