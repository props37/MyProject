package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent

internal sealed interface PickupPointSelectorEvent {
    data class FilterClicked(val filter: ToggleableFilter) : PickupPointSelectorEvent

    data class ViewModeSelectorEvent(val event: TabRowEvent<ViewMode>) : PickupPointSelectorEvent

    data class PickupPointClicked(val pickupPoint: PickupPoint) : PickupPointSelectorEvent

    data object ErrorRefreshClicked : PickupPointSelectorEvent

    data object MyLocationClicked : PickupPointSelectorEvent
}
