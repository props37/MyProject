package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignInByPhoneConfirmationSideEffect : SideEffect {
    data class Navigate(val action: SignInByPhoneConfirmationScreenAction) :
        SignInByPhoneConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) :
        SignInByPhoneConfirmationSideEffect
}
