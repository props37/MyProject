package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PasswordChangeSideEffect : SideEffect {
    data class Navigate(val action: PasswordChangeScreenAction) : PasswordChangeSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PasswordChangeSideEffect
}
