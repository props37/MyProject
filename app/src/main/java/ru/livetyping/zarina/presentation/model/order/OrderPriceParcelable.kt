package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderPrice

@Serializable
@Parcelize
data class OrderPriceParcelable(
    val orderPrice: Int,
    val deliveryPrice: Int,
    val totalPrice: Int,
) : Parcelable {
    fun toOrderPrice(): OrderPrice {
        return OrderPrice(
            orderPrice = orderPrice,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
        )
    }

    companion object {
        fun from(price: OrderPrice): OrderPriceParcelable {
            return OrderPriceParcelable(
                orderPrice = price.orderPrice,
                deliveryPrice = price.deliveryPrice,
                totalPrice = price.totalPrice,
            )
        }
    }
}
