package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType

@Serializable
@Parcelize
public enum class DeliveryMethodTypeParcelable : Parcelable {
    COURIER,
    POST,
    PICKUP_FROM_PICKUP_POINT,
    PICKUP_FROM_STORE_WAREHOUSE,
    PICKUP_FROM_STORE,
    YANDEX_EXPRESS,
    COURIER_EXPRESS;

    public fun toDeliveryMethodType(): DeliveryMethodType {
        return when (this) {
            COURIER -> DeliveryMethodType.COURIER
            POST -> DeliveryMethodType.POST
            PICKUP_FROM_PICKUP_POINT -> DeliveryMethodType.PICKUP_FROM_PICKUP_POINT
            PICKUP_FROM_STORE_WAREHOUSE -> DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE
            PICKUP_FROM_STORE -> DeliveryMethodType.PICKUP_FROM_STORE
            YANDEX_EXPRESS -> DeliveryMethodType.YANDEX_EXPRESS
            COURIER_EXPRESS -> DeliveryMethodType.COURIER_EXPRESS
        }
    }

    public companion object {
        public fun from(type: DeliveryMethodType): DeliveryMethodTypeParcelable {
            return when (type) {
                DeliveryMethodType.COURIER -> COURIER
                DeliveryMethodType.POST -> POST
                DeliveryMethodType.PICKUP_FROM_PICKUP_POINT -> PICKUP_FROM_PICKUP_POINT
                DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE -> PICKUP_FROM_STORE_WAREHOUSE
                DeliveryMethodType.PICKUP_FROM_STORE -> PICKUP_FROM_STORE
                DeliveryMethodType.YANDEX_EXPRESS -> YANDEX_EXPRESS
                DeliveryMethodType.COURIER_EXPRESS -> COURIER_EXPRESS
            }
        }
    }
}
