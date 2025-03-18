package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.DeliveryMethodType

@Serializable
@Parcelize
enum class DeliveryMethodTypeParcelable : Parcelable {
    DELIVERY_SERVICE,
    POST,
    PICKUP,
    PICKUP_IN_STORE,
    RETAIL,
    YANDEX,
    EXPRESS;

    fun toDeliveryMethodType(): DeliveryMethodType {
        return when (this) {
            DELIVERY_SERVICE -> DeliveryMethodType.DELIVERY_SERVICE
            POST -> DeliveryMethodType.POST
            PICKUP -> DeliveryMethodType.PICKUP
            PICKUP_IN_STORE -> DeliveryMethodType.PICKUP_IN_STORE
            RETAIL -> DeliveryMethodType.RETAIL
            YANDEX -> DeliveryMethodType.YANDEX
            EXPRESS -> DeliveryMethodType.EXPRESS
        }
    }

    companion object {
        fun from(type: DeliveryMethodType): DeliveryMethodTypeParcelable {
            return when (type) {
                DeliveryMethodType.DELIVERY_SERVICE -> DELIVERY_SERVICE
                DeliveryMethodType.POST -> POST
                DeliveryMethodType.PICKUP -> PICKUP
                DeliveryMethodType.PICKUP_IN_STORE -> PICKUP_IN_STORE
                DeliveryMethodType.RETAIL -> RETAIL
                DeliveryMethodType.YANDEX -> YANDEX
                DeliveryMethodType.EXPRESS -> EXPRESS
            }
        }
    }
}
