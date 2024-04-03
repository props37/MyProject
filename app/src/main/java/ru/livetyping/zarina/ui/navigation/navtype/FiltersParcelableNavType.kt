package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.filter.FiltersParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.FiltersParcelableType: FiltersParcelableNavType
    get() = FiltersParcelableNavType()

class FiltersParcelableNavType : NavType<FiltersParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): FiltersParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): FiltersParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: FiltersParcelable?) {
        bundle.putParcelable(key, value)
    }
}
