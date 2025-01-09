package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface DetectedCitySideEffect : SideEffect {
    data class Navigate(val action: DetectedCityScreenAction) : DetectedCitySideEffect
}
