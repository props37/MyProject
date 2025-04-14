package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed

internal sealed interface OrderPlacingScreenAction {
    data object BackClicked : OrderPlacingScreenAction

    data object CloseClicked : OrderPlacingScreenAction

    data object ChangeRecipientClicked : OrderPlacingScreenAction

    data object ChangeDeliveryClicked : OrderPlacingScreenAction

    data class GiftCertificateSelected(
        val cartType: CartType,
        val cart: Cart,
    ) : OrderPlacingScreenAction

    data class PaymentStarted(val paymentUrl: Url) : OrderPlacingScreenAction

    data class OrderConfirmed(val order: OrderDetailed) : OrderPlacingScreenAction
}
