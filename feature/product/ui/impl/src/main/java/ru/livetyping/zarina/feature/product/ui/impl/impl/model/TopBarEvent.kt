package ru.livetyping.zarina.feature.product.ui.impl.impl.model

internal sealed interface TopBarEvent {
    data object BackClicked : TopBarEvent

    data object ShareClicked : TopBarEvent
}
