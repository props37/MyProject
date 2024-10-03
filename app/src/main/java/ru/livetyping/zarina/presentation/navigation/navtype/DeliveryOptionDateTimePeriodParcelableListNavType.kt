package ru.livetyping.zarina.presentation.navigation.navtype

import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.livetyping.zarina.presentation.model.checkout.DeliveryOptionParcelable
import ru.livetyping.zarina.presentation.navigation.base.ParcelableListNavType

val NavType.Companion.DeliveryOptionDateTimePeriodParcelableListType: DeliveryOptionDateTimePeriodParcelableListNavType
    get() = DeliveryOptionDateTimePeriodParcelableListNavType()

class DeliveryOptionDateTimePeriodParcelableListNavType :
    ParcelableListNavType<DeliveryOptionParcelable.DateTimePeriod>(
        isNullableAllowed = true,
        itemClass = DeliveryOptionParcelable.DateTimePeriod::class,
        serializer = Json.serializersModule.serializer(),
    )
