package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PasswordRecoverySideEffect : SideEffect {
    data class Navigate(val action: PasswordRecoveryScreenAction) : PasswordRecoverySideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PasswordRecoverySideEffect
}
