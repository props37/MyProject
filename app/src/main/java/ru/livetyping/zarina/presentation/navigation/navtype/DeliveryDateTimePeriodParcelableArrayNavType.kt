package ru.livetyping.zarina.presentation.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable

val NavType.Companion.DeliveryDateTimePeriodParcelableArrayType: DeliveryDateTimePeriodParcelableArrayNavType
    get() = DeliveryDateTimePeriodParcelableArrayNavType()

class DeliveryDateTimePeriodParcelableArrayNavType :
    NavType<Array<DeliveryDateTimePeriodParcelable>>(isNullableAllowed = true) {

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun get(bundle: Bundle, key: String): Array<DeliveryDateTimePeriodParcelable>? {
        return bundle[key] as Array<DeliveryDateTimePeriodParcelable>?
    }

    override fun parseValue(value: String): Array<DeliveryDateTimePeriodParcelable> {
        return Json.decodeFromString(value)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: Array<DeliveryDateTimePeriodParcelable>,
    ) {
        bundle.putParcelableArray(key, value)
    }
}
