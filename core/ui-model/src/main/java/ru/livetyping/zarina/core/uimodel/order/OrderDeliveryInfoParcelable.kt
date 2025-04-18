package ru.livetyping.zarina.core.uimodel.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderDeliveryInfo
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryMethodTypeParcelable

@Serializable
@Parcelize
public data class OrderDeliveryInfoParcelable(
    val type: DeliveryMethodTypeParcelable,
) : Parcelable {
    public fun toOrderDeliveryInfo(): OrderDeliveryInfo {
        return OrderDeliveryInfo(type.toDeliveryMethodType())
    }

    public companion object {
        public fun from(info: OrderDeliveryInfo): OrderDeliveryInfoParcelable {
            return OrderDeliveryInfoParcelable(DeliveryMethodTypeParcelable.from(info.type))
        }
    }
}
