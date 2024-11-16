package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface CitySelectorSideEffect : SideEffect {
    data class Navigate(val action: CitySelectorScreenAction) : CitySelectorSideEffect
}
