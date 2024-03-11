package ru.zarina.zarina.ui.navigation.base

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.domain.old.Filtration

val NavType.Companion.Filtration
    get() = FiltrationNavType

object FiltrationNavType : NavType<Filtration>(true) {

    override fun get(bundle: Bundle, key: String): Filtration? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Filtration {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Filtration) {
        bundle.putString(key, Json.encodeToString(value))
    }

}
