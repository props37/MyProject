package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable

val NavType.Companion.ProductOfferParcelableArrayType: ProductOfferParcelableArrayNavType
    get() = ProductOfferParcelableArrayNavType()

class ProductOfferParcelableArrayNavType :
    NavType<Array<ProductOfferParcelable>?>(isNullableAllowed = true) {

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun get(bundle: Bundle, key: String): Array<ProductOfferParcelable>? {
        return bundle[key] as Array<ProductOfferParcelable>?
    }

    override fun parseValue(value: String): Array<ProductOfferParcelable> {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Array<ProductOfferParcelable>?) {
        bundle.putParcelableArray(key, value)
    }
}
