package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PhoneChangeSideEffect : SideEffect {
    data class Navigate(val action: PhoneChangeScreenAction) : PhoneChangeSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PhoneChangeSideEffect
}
