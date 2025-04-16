package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class GiftCertificateNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val cartFinalPrice: Int,
) : NavigationEntry {
    companion object {
        fun from(cartType: CartType, cartFinalPrice: Int): GiftCertificateNavEntry {
            return GiftCertificateNavEntry(
                cartType = CartTypeParcelable.from(cartType),
                cartFinalPrice = cartFinalPrice,
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val cartType = parcelableNavType<CartTypeParcelable>(isNullableAllowed = false)
            return mapOf(typeOf<CartTypeParcelable>() to cartType)
        }
    }
}
