package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search

internal sealed interface AddressSearchEvent {
    data object CloseClicked : AddressSearchEvent

    data class AddressItemClicked(
        val item: AddressSearchItem,
        val type: AddressSearchType,
    ) : AddressSearchEvent

    data class ErrorRefreshClicked(val type: AddressSearchType) : AddressSearchEvent
}
