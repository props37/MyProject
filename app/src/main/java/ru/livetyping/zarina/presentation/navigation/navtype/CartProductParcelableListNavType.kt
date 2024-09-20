package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableListNavType

val NavType.Companion.CartProductParcelableListType: CartProductParcelableListNavType
    get() = CartProductParcelableListNavType()

class CartProductParcelableListNavType : ParcelableListNavType<CartProductParcelable>(
    isNullableAllowed = true,
    itemClass = CartProductParcelable::class,
    serializer = Json.serializersModule.serializer(),
)
