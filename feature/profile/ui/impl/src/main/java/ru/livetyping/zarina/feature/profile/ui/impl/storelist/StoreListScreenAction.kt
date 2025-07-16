package ru.livetyping.zarina.feature.profile.ui.impl.storelist

internal sealed interface StoreListScreenAction {
    data object BackClicked : StoreListScreenAction
}
