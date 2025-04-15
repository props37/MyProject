package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class OrderPlacingNavActions(
    val onBackClicked: () -> Unit,
    val onCloseClicked: () -> Unit,
    val onChangeRecipientClicked: () -> Unit,
    val onChangeDeliveryClicked: () -> Unit,
    val onGiftCertificateSelected: (cartType: CartType, cart: Cart) -> Unit,
    val onPaymentStarted: (paymentUrl: Url) -> Unit,
    val onOrderConfirmed: (order: OrderDetailed) -> Unit,
) : NavigationActions
