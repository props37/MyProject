package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface PasswordRecoverySideEffect : SideEffect {
    data class Navigate(val action: PasswordRecoveryScreenAction) : PasswordRecoverySideEffect
}
