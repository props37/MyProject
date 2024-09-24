package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.order.DeliveryMethodType

sealed class CheckoutPickupPointDeliveryScreenAction {
    data object ScreenClosed : CheckoutPickupPointDeliveryScreenAction()

    data object CheckoutClosed : CheckoutPickupPointDeliveryScreenAction()

    data object LocationPermissionRequired : CheckoutPickupPointDeliveryScreenAction()

    data class PickupPointSelected(
        val pickupPoint: PickupPoint,
        val cartType: CartType,
        val step: Int,
        val deliveryMethodType: DeliveryMethodType,
        val customer: Customer,
    ) : CheckoutPickupPointDeliveryScreenAction()
}
