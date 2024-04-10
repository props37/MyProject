package ru.livetyping.zarina.ui.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.ui.model.filter.ListFilterParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.ListFilterParcelableType: ListFilterParcelableNavType
    get() = ListFilterParcelableNavType()

class ListFilterParcelableNavType : NavType<ListFilterParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): ListFilterParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): ListFilterParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: ListFilterParcelable?) {
        bundle.putParcelable(key, value)
    }
}
