package ru.livetyping.zarina.feature.home.ui.impl.impl

import ru.livetyping.zarina.core.ui.common.sideeffect.SideEffect

internal sealed interface HomeScreenSideEffect : SideEffect {
    data class Navigate(val action: HomeScreenAction) : HomeScreenSideEffect
}
