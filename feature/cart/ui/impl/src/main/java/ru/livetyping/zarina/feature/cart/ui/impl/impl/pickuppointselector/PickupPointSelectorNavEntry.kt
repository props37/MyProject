package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryMethodParcelable
import ru.livetyping.zarina.core.uimodel.checkout.RecipientParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class PickupPointSelectorNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val checkoutStep: Int,
    val recipient: RecipientParcelable,
    val deliveryMethod: DeliveryMethodParcelable,
) : NavigationEntry {
    companion object {
        fun from(
            cartType: CartType,
            checkoutStep: Int,
            recipient: Recipient,
            deliveryMethod: DeliveryMethod,
        ): PickupPointSelectorNavEntry {
            return PickupPointSelectorNavEntry(
                cartType = CartTypeParcelable.from(cartType),
                checkoutStep = checkoutStep,
                recipient = RecipientParcelable.from(recipient),
                deliveryMethod = DeliveryMethodParcelable.from(deliveryMethod),
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val recipientType = parcelableNavType<RecipientParcelable>(
                isNullableAllowed = false,
            )
            val deliveryMethodType = parcelableNavType<DeliveryMethodParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(
                typeOf<CartTypeParcelable>() to NavType.EnumType(CartTypeParcelable::class.java),
                typeOf<RecipientParcelable>() to recipientType,
                typeOf<DeliveryMethodParcelable>() to deliveryMethodType,
            )
        }
    }
}
