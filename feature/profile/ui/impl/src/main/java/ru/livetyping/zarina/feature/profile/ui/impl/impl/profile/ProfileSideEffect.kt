package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ProfileSideEffect : SideEffect {
    data class Navigate(val action: ProfileScreenAction) : ProfileSideEffect

    data class OpenUrl(val url: Text) : ProfileSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ProfileSideEffect
}
