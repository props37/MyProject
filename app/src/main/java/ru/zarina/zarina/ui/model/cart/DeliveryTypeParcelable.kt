package ru.zarina.zarina.ui.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.cart.DeliveryType

@Serializable
@Parcelize
enum class DeliveryTypeParcelable : Parcelable {
    DELIVERY,
    PICK_UP_FROM_SHOP;

    fun toDeliveryType(): DeliveryType = when (this) {
        DELIVERY -> DeliveryType.DELIVERY
        PICK_UP_FROM_SHOP -> DeliveryType.PICK_UP_FROM_SHOP
    }

    companion object {
        fun from(deliveryType: DeliveryType): DeliveryTypeParcelable = when (deliveryType) {
            DeliveryType.DELIVERY -> DELIVERY
            DeliveryType.PICK_UP_FROM_SHOP -> PICK_UP_FROM_SHOP
        }
    }
}
