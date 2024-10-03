package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableNavType

val NavType.Companion.CheckoutParamsParcelableType: CheckoutParamsParcelableNavType
    get() = CheckoutParamsParcelableNavType()

class CheckoutParamsParcelableNavType : ParcelableNavType<CheckoutParamsParcelable?>(
    isNullableAllowed = true,
    serializer = Json.serializersModule.serializer(),
)
