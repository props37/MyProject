package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
enum class CourierDeliveryDateTimeSelectorType : Parcelable { DATE, TIME }
