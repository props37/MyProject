package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent

internal sealed interface PickupPointSelectorEvent {
    data class FilterClicked(val filter: ToggleableFilter) : PickupPointSelectorEvent

    data class ViewModeSelectorEvent(val event: TabRowEvent<ViewMode>) : PickupPointSelectorEvent
}
