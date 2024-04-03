package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.ProductOfferParcelableType: ProductOfferParcelableNavType
    get() = ProductOfferParcelableNavType()

class ProductOfferParcelableNavType : NavType<ProductOfferParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ProductOfferParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): ProductOfferParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: ProductOfferParcelable?) {
        bundle.putParcelable(key, value)
    }
}
