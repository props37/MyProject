package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model

internal sealed interface StoreListEvent {
    data object BackClicked : StoreListEvent

    data object ErrorRefreshClicked : StoreListEvent
}
