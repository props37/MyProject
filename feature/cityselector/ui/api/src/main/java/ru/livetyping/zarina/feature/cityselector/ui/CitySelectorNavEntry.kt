package ru.livetyping.zarina.feature.cityselector.ui

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
public data class CitySelectorNavEntry(
    val title: Text? = null,
    val currentCity: CityParcelable? = null,
) {
    public companion object {
        public fun typeMap(): Map<KType, NavType<*>> {
            val textType = object : ParcelableNavType<Text?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            ) {}
            val cityType = object : ParcelableNavType<CityParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            ) {}
            return mapOf(
                typeOf<Text?>() to textType,
                typeOf<CityParcelable?>() to cityType,
            )
        }
    }
}
