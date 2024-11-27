package ru.livetyping.zarina.core.uikit.order

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

public val OrderStatus.color: Color
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
