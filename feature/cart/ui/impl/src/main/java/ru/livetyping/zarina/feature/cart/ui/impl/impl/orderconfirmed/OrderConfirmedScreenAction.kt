package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import ru.livetyping.zarina.core.domain.model.common.Url

internal sealed interface OrderConfirmedScreenAction {
    data object ReturnToHomeClicked : OrderConfirmedScreenAction

    data class PayClicked(val paymentUrl: Url) : OrderConfirmedScreenAction
}
