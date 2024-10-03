package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.filter.ListFilterParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.ListFilterParcelableType: ListFilterParcelableNavType
    get() = ListFilterParcelableNavType()

class ListFilterParcelableNavType : ParcelableNavType<ListFilterParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
