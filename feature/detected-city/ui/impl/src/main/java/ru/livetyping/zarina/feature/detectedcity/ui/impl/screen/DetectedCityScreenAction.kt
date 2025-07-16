package ru.livetyping.zarina.feature.detectedcity.ui.impl.screen

internal sealed interface DetectedCityScreenAction {
    data object CloseClicked : DetectedCityScreenAction
}
