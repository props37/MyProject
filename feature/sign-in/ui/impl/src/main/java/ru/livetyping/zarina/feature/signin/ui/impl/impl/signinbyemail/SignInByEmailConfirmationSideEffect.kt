package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignInByEmailConfirmationSideEffect : SideEffect {
    data class Navigate(val action: SignInByEmailConfirmationScreenAction) :
        SignInByEmailConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) :
        SignInByEmailConfirmationSideEffect
}
