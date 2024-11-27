package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignInSideEffect : SideEffect {
    data class Navigate(val action: SignInScreenAction) : SignInSideEffect

    data object FreeFocus : SignInSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SignInSideEffect
}
