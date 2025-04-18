package ru.livetyping.zarina.core.uimodel.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderPrice
import java.math.BigDecimal

@Serializable
@Parcelize
public data class OrderPriceParcelable(
    val orderPrice: String,
    val deliveryPrice: String,
    val totalPrice: String,
) : Parcelable {
    public fun toOrderPrice(): OrderPrice {
        return OrderPrice(
            orderPrice = BigDecimal(orderPrice),
            deliveryPrice = BigDecimal(deliveryPrice),
            totalPrice = BigDecimal(totalPrice),
        )
    }

    public companion object {
        public fun from(price: OrderPrice): OrderPriceParcelable {
            return OrderPriceParcelable(
                orderPrice = price.orderPrice.toString(),
                deliveryPrice = price.deliveryPrice.toString(),
                totalPrice = price.totalPrice.toString(),
            )
        }
    }
}
