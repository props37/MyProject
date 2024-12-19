package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignUpSideEffect : SideEffect {
    data class Navigate(val action: SignUpScreenAction) : SignUpSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SignUpSideEffect
}
