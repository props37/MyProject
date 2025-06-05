package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

internal sealed interface ProductEvent {
    data object BackClicked : ProductEvent

    data object ProductRefreshTriggered : ProductEvent
}
