package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
internal enum class DeliveryOptionDateTimeSelectorType : Parcelable {
    DATE,
    TIME,
}
