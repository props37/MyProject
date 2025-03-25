package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import ru.livetyping.zarina.core.uimodel.checkout.RecipientParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class DeliveryMethodSelectorNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val checkoutStep: Int,
    val recipient: RecipientParcelable,
) : NavigationEntry {
    companion object {
        fun from(
            cartType: CartType,
            checkoutStep: Int,
            recipient: Recipient,
        ): DeliveryMethodSelectorNavEntry {
            return DeliveryMethodSelectorNavEntry(
                cartType = CartTypeParcelable.from(cartType),
                checkoutStep = checkoutStep,
                recipient = RecipientParcelable.from(recipient),
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val recipientType = parcelableNavType<RecipientParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(
                typeOf<CartTypeParcelable>() to NavType.EnumType(CartTypeParcelable::class.java),
                typeOf<RecipientParcelable>() to recipientType,
            )
        }
    }
}
