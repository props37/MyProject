package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderDeliveryInfo

@Serializable
@Parcelize
data class OrderDeliveryInfoParcelable(
    val type: DeliveryMethodTypeParcelable,
) : Parcelable {
    fun toOrderDeliveryInfo(): OrderDeliveryInfo {
        return OrderDeliveryInfo(type.toDeliveryMethodType())
    }

    companion object {
        fun from(info: OrderDeliveryInfo): OrderDeliveryInfoParcelable {
            return OrderDeliveryInfoParcelable(DeliveryMethodTypeParcelable.from(info.type))
        }
    }
}
