package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.core.uimodel.checkout.RecipientParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class PickupStoreSelectorNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val checkoutStep: Int,
    val recipient: RecipientParcelable,
    val deliveryMethodType: DeliveryMethodTypeParcelable,
) : NavigationEntry {
    companion object {
        fun from(
            cartType: CartType,
            checkoutStep: Int,
            recipient: Recipient,
            deliveryMethodType: DeliveryMethodType,
        ): PickupStoreSelectorNavEntry {
            return PickupStoreSelectorNavEntry(
                cartType = CartTypeParcelable.from(cartType),
                checkoutStep = checkoutStep,
                recipient = RecipientParcelable.from(recipient),
                deliveryMethodType = DeliveryMethodTypeParcelable.from(deliveryMethodType),
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val recipientType = ParcelableNavType<RecipientParcelable>(
                isNullableAllowed = false,
                serializer = kotlinx.serialization.serializer(),
            )
            return mapOf(
                typeOf<CartTypeParcelable>() to NavType.EnumType(CartTypeParcelable::class.java),
                typeOf<RecipientParcelable>() to recipientType,
                typeOf<DeliveryMethodTypeParcelable>() to NavType.EnumType(DeliveryMethodTypeParcelable::class.java),
            )
        }
    }
}
