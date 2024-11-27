package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.resource.R as RCommon

public val OrderStatus.nameResId: Int
    get() = when (this) {
        OrderStatus.OPENED -> RCommon.string.res_order_status_accepted
        OrderStatus.APPROVED -> RCommon.string.res_order_status_confirmed
        OrderStatus.PAID -> RCommon.string.res_order_status_confirmed
        OrderStatus.IN_TRANSIT -> RCommon.string.res_order_status_on_the_way
        OrderStatus.DELIVERED -> RCommon.string.res_order_status_delivered
        OrderStatus.READY_FOR_PICKUP -> RCommon.string.res_order_status_ready_for_pickup
        OrderStatus.CANCELLED -> RCommon.string.res_order_status_cancelled
        OrderStatus.REFUNDING -> RCommon.string.res_order_status_refund
        OrderStatus.APPROVED_TO_REFUND -> RCommon.string.res_order_status_refund
        OrderStatus.REFUNDED -> RCommon.string.res_order_status_refund
        OrderStatus.NOT_REFUNDABLE -> RCommon.string.res_order_status_not_refundable
    }
