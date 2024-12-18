package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface PhoneConfirmationSideEffect : SideEffect {
    data class Navigate(val action: PhoneConfirmationScreenAction) : PhoneConfirmationSideEffect
}
