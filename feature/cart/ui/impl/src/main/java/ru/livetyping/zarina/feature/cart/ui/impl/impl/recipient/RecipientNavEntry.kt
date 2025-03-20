package ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class RecipientNavEntry private constructor(
    val cartType: CartTypeParcelable,
    val step: Int,
) : NavigationEntry {
    companion object {
        fun from(cartType: CartType, step: Int = 1): RecipientNavEntry {
            val cartTypeParcelable = CartTypeParcelable.from(cartType)
            return RecipientNavEntry(cartTypeParcelable, step)
        }

        fun typeMap(): Map<KType, NavType<*>> {
            return mapOf(
                typeOf<CartTypeParcelable>() to NavType.EnumType(CartTypeParcelable::class.java),
            )
        }
    }
}
