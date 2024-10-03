package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.StoreParcelableType: StoreParcelableNavType
    get() = StoreParcelableNavType()

class StoreParcelableNavType : ParcelableNavType<StoreParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
