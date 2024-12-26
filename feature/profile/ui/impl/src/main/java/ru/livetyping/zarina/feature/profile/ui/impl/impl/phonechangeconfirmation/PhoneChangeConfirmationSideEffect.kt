package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PhoneChangeConfirmationSideEffect : SideEffect {
    data class Navigate(val action: PhoneChangeConfirmationScreenAction) :
        PhoneChangeConfirmationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PhoneChangeConfirmationSideEffect
}
