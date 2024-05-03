package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.product.ProductItemParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.ProductParcelableType: ProductParcelableNavType
    get() = ProductParcelableNavType()

class ProductParcelableNavType : NavType<ProductItemParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ProductItemParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): ProductItemParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: ProductItemParcelable?) {
        bundle.putParcelable(key, value)
    }
}
