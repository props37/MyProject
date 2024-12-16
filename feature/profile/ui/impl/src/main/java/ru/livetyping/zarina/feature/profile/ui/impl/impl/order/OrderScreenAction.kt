package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

internal sealed interface OrderScreenAction {
    data object BackClicked : OrderScreenAction
}
