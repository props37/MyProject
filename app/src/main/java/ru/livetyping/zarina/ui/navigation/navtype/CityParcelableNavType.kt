package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.geography.CityParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.CityParcelableType: CityParcelableNavType
    get() = CityParcelableNavType()

class CityParcelableNavType : NavType<CityParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): CityParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): CityParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: CityParcelable?) {
        bundle.putParcelable(key, value)
    }
}
