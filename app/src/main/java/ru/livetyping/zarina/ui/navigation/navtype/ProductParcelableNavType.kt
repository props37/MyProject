package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.product.ProductParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.ProductParcelableType: ProductParcelableNavType
    get() = ProductParcelableNavType()

class ProductParcelableNavType : NavType<ProductParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ProductParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): ProductParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: ProductParcelable?) {
        bundle.putParcelable(key, value)
    }
}
