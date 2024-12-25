package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PasswordChangingSideEffect : SideEffect {
    data class Navigate(val action: PasswordChangingScreenAction) : PasswordChangingSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PasswordChangingSideEffect
}
