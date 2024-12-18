package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SignUpConfirmationSideEffect : SideEffect {
    data class Navigate(val action: SignUpConfirmationScreenAction) : SignUpConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SignUpConfirmationSideEffect
}
