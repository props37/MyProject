package ru.livetyping.zarina.ui.common.util.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderStatus
import ru.livetyping.zarina.ui.theme.UiKitTheme

val OrderStatus.nameResId: Int
    get() = when (this) {
        OrderStatus.OPENED -> R.string.order_status_accepted
        OrderStatus.APPROVED -> R.string.order_status_confirmed
        OrderStatus.PAID -> R.string.order_status_confirmed
        OrderStatus.IN_TRANSIT -> R.string.order_status_on_the_way
        OrderStatus.DELIVERED -> R.string.order_status_delivered
        OrderStatus.READY_FOR_PICKUP -> R.string.order_status_ready_for_pickup
        OrderStatus.CANCELLED -> R.string.order_status_cancelled
        OrderStatus.REFUNDING -> R.string.order_status_refund
        OrderStatus.APPROVED_TO_REFUND -> R.string.order_status_refund
        OrderStatus.REFUNDED -> R.string.order_status_refund
        OrderStatus.NOT_REFUNDABLE -> R.string.order_status_not_refundable
    }

val OrderStatus.color: Color
    @Composable
    get() = when (this) {
        OrderStatus.OPENED -> UiKitTheme.colors.text.label.warning
        OrderStatus.APPROVED -> UiKitTheme.colors.text.label.warning
        OrderStatus.PAID -> UiKitTheme.colors.text.label.warning
        OrderStatus.IN_TRANSIT -> UiKitTheme.colors.text.label.warning
        OrderStatus.DELIVERED -> UiKitTheme.colors.text.label.success
        OrderStatus.READY_FOR_PICKUP -> UiKitTheme.colors.text.label.success
        OrderStatus.CANCELLED -> UiKitTheme.colors.text.label.danger
        OrderStatus.REFUNDING -> UiKitTheme.colors.text.label.warning
        OrderStatus.APPROVED_TO_REFUND -> UiKitTheme.colors.text.label.warning
        OrderStatus.REFUNDED -> UiKitTheme.colors.text.label.warning
        OrderStatus.NOT_REFUNDABLE -> UiKitTheme.colors.text.label.danger
    }
