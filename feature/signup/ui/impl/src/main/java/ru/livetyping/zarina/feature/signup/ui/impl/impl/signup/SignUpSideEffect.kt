package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface SignUpSideEffect : SideEffect {
    data class Navigate(val action: SignUpScreenAction) : SignUpSideEffect
}
