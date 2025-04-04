package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

internal sealed interface DeliveryAddressSelectorEvent {
    data object StreetSelectorClicked : DeliveryAddressSelectorEvent

    data object BuildingSelectorClicked : DeliveryAddressSelectorEvent
}
