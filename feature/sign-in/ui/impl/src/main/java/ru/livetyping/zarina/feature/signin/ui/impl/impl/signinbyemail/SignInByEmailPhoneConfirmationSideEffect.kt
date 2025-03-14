package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignInByEmailPhoneConfirmationSideEffect : SideEffect {
    data class Navigate(val action: SignInByEmailPhoneConfirmationScreenAction) :
        SignInByEmailPhoneConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) :
        SignInByEmailPhoneConfirmationSideEffect
}
