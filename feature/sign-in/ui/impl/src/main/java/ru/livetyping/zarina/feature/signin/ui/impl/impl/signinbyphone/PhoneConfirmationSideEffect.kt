package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PhoneConfirmationSideEffect : SideEffect {
    data class Navigate(val action: PhoneConfirmationScreenAction) : PhoneConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PhoneConfirmationSideEffect
}
