package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.CustomerParcelableType: CustomerParcelableNavType
    get() = CustomerParcelableNavType()

class CustomerParcelableNavType : ParcelableNavType<CustomerParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
