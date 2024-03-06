package ru.zarina.zarina.ui.navigation.rework.base.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.util.platform.BundleCompat

val NavType.Companion.TextType: TextNavType
    get() = TextNavType()

class TextNavType : NavType<Text?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Text? {
        return BundleCompat.getParcelable(bundle, key)
    }

    override fun parseValue(value: String): Text? {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Text?) {
        bundle.putParcelable(key, value)
    }
}
