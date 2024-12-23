package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface EmailChangingSideEffect : SideEffect {
    data class Navigate(val action: EmailChangingScreenAction) : EmailChangingSideEffect
}
