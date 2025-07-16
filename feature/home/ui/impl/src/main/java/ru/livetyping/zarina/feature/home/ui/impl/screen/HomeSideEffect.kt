package ru.livetyping.zarina.feature.home.ui.impl.screen

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface HomeSideEffect : SideEffect {
    data class Navigate(val action: HomeScreenAction) : HomeSideEffect
}
