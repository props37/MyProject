package ru.livetyping.zarina.presentation.navigation.navtype

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.util.platform.BundleCompat

val NavType.Companion.StoreParcelableType: StoreParcelableNavType
    get() = StoreParcelableNavType()

class StoreParcelableNavType : NavType<StoreParcelable?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): StoreParcelable? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): StoreParcelable {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: StoreParcelable?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: StoreParcelable?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}
