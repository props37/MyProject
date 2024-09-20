package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.CartProductParcelableType: CartProductParcelableNavType
    get() = CartProductParcelableNavType()

class CartProductParcelableNavType : ParcelableNavType<CartProductParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
