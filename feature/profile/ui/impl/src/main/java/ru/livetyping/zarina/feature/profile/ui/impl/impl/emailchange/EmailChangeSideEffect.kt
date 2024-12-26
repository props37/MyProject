package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface EmailChangeSideEffect : SideEffect {
    data class Navigate(val action: EmailChangeScreenAction) : EmailChangeSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : EmailChangeSideEffect
}
