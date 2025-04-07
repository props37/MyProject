package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption

internal sealed interface DeliveryAddressSelectorEvent {
    data object StreetSelectorClicked : DeliveryAddressSelectorEvent

    data object BuildingSelectorClicked : DeliveryAddressSelectorEvent

    data class DeliveryOptionClicked(val deliveryOption: DeliveryOption) :
        DeliveryAddressSelectorEvent

    data class DeliveryOptionDateClicked(val deliveryOption: DeliveryOption) :
        DeliveryAddressSelectorEvent

    data class DeliveryOptionTimeClicked(val deliveryOption: DeliveryOption) :
        DeliveryAddressSelectorEvent

    data class ShowDeliveryOptionDetails(val deliveryOption: DeliveryOption) :
        DeliveryAddressSelectorEvent

    data object ContinueClicked : DeliveryAddressSelectorEvent

    data object ErrorRefreshClicked : DeliveryAddressSelectorEvent

    data object GenericBottomSheetClosed : DeliveryAddressSelectorEvent
}
