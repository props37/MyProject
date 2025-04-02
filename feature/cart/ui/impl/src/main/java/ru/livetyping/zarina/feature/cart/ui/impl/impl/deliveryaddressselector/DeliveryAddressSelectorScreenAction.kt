package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

internal sealed interface DeliveryAddressSelectorScreenAction {
    data object BackClicked : DeliveryAddressSelectorScreenAction

    data object CloseClicked : DeliveryAddressSelectorScreenAction
}
