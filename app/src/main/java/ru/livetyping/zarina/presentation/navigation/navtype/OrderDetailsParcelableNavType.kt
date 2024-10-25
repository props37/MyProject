package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.order.OrderDetailsParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.OrderDetailsParcelableType: OrderDetailsParcelableNavType
    get() = OrderDetailsParcelableNavType()

class OrderDetailsParcelableNavType : ParcelableNavType<OrderDetailsParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
