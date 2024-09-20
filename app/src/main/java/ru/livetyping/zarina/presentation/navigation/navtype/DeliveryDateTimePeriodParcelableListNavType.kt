package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableListNavType

val NavType.Companion.DeliveryDateTimePeriodParcelableListType: DeliveryDateTimePeriodParcelableListNavType
    get() = DeliveryDateTimePeriodParcelableListNavType()

class DeliveryDateTimePeriodParcelableListNavType :
    ParcelableListNavType<DeliveryDateTimePeriodParcelable>(
        isNullableAllowed = true,
        itemClass = DeliveryDateTimePeriodParcelable::class,
        serializer = Json.serializersModule.serializer(),
    )
