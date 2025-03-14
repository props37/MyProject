package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignInByPhonePhoneConfirmationSideEffect : SideEffect {
    data class Navigate(val action: SignInByPhonePhoneConfirmationScreenAction) :
        SignInByPhonePhoneConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) :
        SignInByPhonePhoneConfirmationSideEffect
}
