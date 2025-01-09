package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl

internal sealed interface DetectedCityScreenAction {
    data object CloseClicked : DetectedCityScreenAction
}
