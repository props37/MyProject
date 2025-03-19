package ru.livetyping.zarina.core.uimodel.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType

@Serializable
@Parcelize
public enum class CartTypeParcelable : Parcelable {
    DELIVERY,
    PICKUP;

    public fun toCartType(): CartType = when (this) {
        DELIVERY -> CartType.DELIVERY
        PICKUP -> CartType.PICKUP
    }

    public companion object {
        public fun from(cartType: CartType): CartTypeParcelable = when (cartType) {
            CartType.DELIVERY -> DELIVERY
            CartType.PICKUP -> PICKUP
        }
    }
}
