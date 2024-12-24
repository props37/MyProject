package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface EmailChangingSideEffect : SideEffect {
    data class Navigate(val action: EmailChangingScreenAction) : EmailChangingSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : EmailChangingSideEffect
}
