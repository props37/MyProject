package ru.livetyping.zarina.presentation.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.CartProductParcelableType: CartProductParcelableNavType
    get() = CartProductParcelableNavType()

class CartProductParcelableNavType : NavType<CartProductParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): CartProductParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): CartProductParcelable? {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: CartProductParcelable?) {
        bundle.putParcelable(key, value)
    }
}
