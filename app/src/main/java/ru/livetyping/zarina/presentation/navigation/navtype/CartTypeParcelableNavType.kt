package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.CartTypeParcelableType: CartTypeParcelableNavType
    get() = CartTypeParcelableNavType()

class CartTypeParcelableNavType : ParcelableNavType<CartTypeParcelable?>(
    isNullableAllowed = true,
    serializer = serializersModule.serializer(),
)
