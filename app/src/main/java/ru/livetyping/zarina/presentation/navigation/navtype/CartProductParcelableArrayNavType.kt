package ru.livetyping.zarina.presentation.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable

val NavType.Companion.CartProductParcelableArrayType: CartProductParcelableArrayNavType
    get() = CartProductParcelableArrayNavType()

class CartProductParcelableArrayNavType :
    NavType<Array<CartProductParcelable>?>(isNullableAllowed = true) {

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun get(bundle: Bundle, key: String): Array<CartProductParcelable>? {
        return bundle[key] as Array<CartProductParcelable>?
    }

    override fun parseValue(value: String): Array<CartProductParcelable> {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Array<CartProductParcelable>?) {
        bundle.putParcelableArray(key, value)
    }
}
