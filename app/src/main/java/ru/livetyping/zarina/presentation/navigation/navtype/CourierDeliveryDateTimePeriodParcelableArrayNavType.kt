package ru.livetyping.zarina.presentation.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.presentation.model.checkout.CourierDeliveryDateTimePeriodParcelable

@Suppress("MaxLineLength")
val NavType.Companion.CourierDeliveryDateTimePeriodParcelableArrayType: CourierDeliveryDateTimePeriodParcelableArrayNavType
    get() = CourierDeliveryDateTimePeriodParcelableArrayNavType()

class CourierDeliveryDateTimePeriodParcelableArrayNavType :
    NavType<Array<CourierDeliveryDateTimePeriodParcelable>>(isNullableAllowed = true) {

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun get(bundle: Bundle, key: String): Array<CourierDeliveryDateTimePeriodParcelable>? {
        return bundle[key] as Array<CourierDeliveryDateTimePeriodParcelable>?
    }

    override fun parseValue(value: String): Array<CourierDeliveryDateTimePeriodParcelable> {
        return Json.decodeFromString(value)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: Array<CourierDeliveryDateTimePeriodParcelable>,
    ) {
        bundle.putParcelableArray(key, value)
    }
}
