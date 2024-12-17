package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface OtpSideEffect : SideEffect {
    data class Navigate(val action: OtpScreenAction) : OtpSideEffect

    data object HideKeyboard : OtpSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : OtpSideEffect
}
