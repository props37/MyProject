package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.CityParcelableType: CityParcelableNavType
    get() = CityParcelableNavType()

class CityParcelableNavType : ParcelableNavType<CityParcelable?>(
    isNullableAllowed = true,
    serializer = serializersModule.serializer(),
)
