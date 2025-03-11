package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

internal sealed interface AvailabilityInStoresEvent {
    data object BackClicked : AvailabilityInStoresEvent

    data class SizeClicked(val size: Size) : AvailabilityInStoresEvent

    data object ErrorRefreshClicked : AvailabilityInStoresEvent
}
