package ru.livetyping.zarina.presentation.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartType

@Serializable
@Parcelize
enum class CartTypeParcelable : Parcelable {
    DELIVERY,
    PICKUP;

    fun toCartType(): CartType = when (this) {
        DELIVERY -> CartType.DELIVERY
        PICKUP -> CartType.PICKUP
    }

    companion object {
        fun from(cartType: CartType): CartTypeParcelable = when (cartType) {
            CartType.DELIVERY -> DELIVERY
            CartType.PICKUP -> PICKUP
        }
    }
}
