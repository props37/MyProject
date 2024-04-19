package ru.livetyping.zarina.ui.common.util.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.ui.theme.UiKitTheme

val Order.Status.nameResId: Int
    get() = when (this) {
        Order.Status.OPENED -> R.string.order_status_accepted
        Order.Status.APPROVED -> R.string.order_status_confirmed
        Order.Status.PAID -> R.string.order_status_confirmed
        Order.Status.IN_TRANSIT -> R.string.order_status_on_the_way
        Order.Status.DELIVERED -> R.string.order_status_delivered
        Order.Status.READY_FOR_PICKUP -> R.string.order_status_ready_for_pickup
        Order.Status.CANCELLED -> R.string.order_status_cancelled
        Order.Status.REFUNDING -> R.string.order_status_refund
        Order.Status.APPROVED_TO_REFUND -> R.string.order_status_refund
        Order.Status.REFUNDED -> R.string.order_status_refund
        Order.Status.NOT_REFUNDABLE -> R.string.order_status_not_refundable
    }

val Order.Status.color: Color
    @Composable
    get() = when (this) {
        Order.Status.OPENED -> UiKitTheme.colors.text.label.warning
        Order.Status.APPROVED -> UiKitTheme.colors.text.label.warning
        Order.Status.PAID -> UiKitTheme.colors.text.label.warning
        Order.Status.IN_TRANSIT -> UiKitTheme.colors.text.label.warning
        Order.Status.DELIVERED -> UiKitTheme.colors.text.label.success
        Order.Status.READY_FOR_PICKUP -> UiKitTheme.colors.text.label.success
        Order.Status.CANCELLED -> UiKitTheme.colors.text.label.danger
        Order.Status.REFUNDING -> UiKitTheme.colors.text.label.warning
        Order.Status.APPROVED_TO_REFUND -> UiKitTheme.colors.text.label.warning
        Order.Status.REFUNDED -> UiKitTheme.colors.text.label.warning
        Order.Status.NOT_REFUNDABLE -> UiKitTheme.colors.text.label.danger
    }
