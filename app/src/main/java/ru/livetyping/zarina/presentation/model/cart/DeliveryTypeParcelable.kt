package ru.livetyping.zarina.presentation.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.DeliveryType

@Serializable
@Parcelize
enum class DeliveryTypeParcelable : Parcelable {
    DELIVERY,
    PICK_UP_FROM_STORE;

    fun toDeliveryType(): DeliveryType = when (this) {
        DELIVERY -> DeliveryType.DELIVERY
        PICK_UP_FROM_STORE -> DeliveryType.PICK_UP_FROM_STORE
    }

    companion object {
        fun from(deliveryType: DeliveryType): DeliveryTypeParcelable = when (deliveryType) {
            DeliveryType.DELIVERY -> DELIVERY
            DeliveryType.PICK_UP_FROM_STORE -> PICK_UP_FROM_STORE
        }
    }
}
