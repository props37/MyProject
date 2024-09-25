package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPoint.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.checkout.PickupPointInfo
import ru.livetyping.zarina.presentation.model.location.LocationParcelable

@Serializable
@Parcelize
data class PickupPointParcelable(
    val id: Long,
    val title: String,
    val address: String,
    val location: LocationParcelable,
    val isFittingAvailable: Boolean,
    val isPaymentByCardAvailable: Boolean,
    val availablePaymentMethods: Set<PaymentMethod>,
    val schedule: String?,
    val expectedDeliveryDate: String?,
    val storageTime: Int?,
    val deliveryTypes: List<DeliveryType>?,
) : Parcelable {
    fun toPickupPointInfo(): PickupPointInfo {
        return PickupPointInfo(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable,
            isPaymentByCardAvailable = isPaymentByCardAvailable,
            availablePaymentMethods = availablePaymentMethods,
        )
    }

    fun toPickupPointDetails(): PickupPointDetails {
        if (
            schedule == null || expectedDeliveryDate == null
            || storageTime == null || deliveryTypes == null
        ) {
            error("Can not cast $this to PickupPointDetails because some properties are null")
        }

        return PickupPointDetails(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable,
            isPaymentByCardAvailable = isPaymentByCardAvailable,
            availablePaymentMethods = availablePaymentMethods,
            schedule = schedule,
            expectedDeliveryDate = expectedDeliveryDate,
            storageTime = storageTime,
            deliveryTypes = deliveryTypes.map { it.toPickupPointDeliveryType() },
        )
    }

    @Serializable
    @Parcelize
    data class DeliveryType(
        val id: String,
        val title: String,
        val description: String,
        val dateTimePeriods: List<DateTimePeriod>,
    ) : Parcelable {
        fun toPickupPointDeliveryType(): PickupPointDetails.DeliveryType {
            return PickupPointDetails.DeliveryType(
                id = PickupPointDetails.DeliveryType.Id(id),
                title = title,
                description = description,
                dateTimePeriods = dateTimePeriods.map { it.toPickupPointDateTimePeriod() },
            )
        }

        @Serializable
        @Parcelize
        data class DateTimePeriod(
            val id: Long,
            val title: String,
        ) : Parcelable {
            fun toPickupPointDateTimePeriod(): PickupPointDetails.DeliveryType.DateTimePeriod {
                return PickupPointDetails.DeliveryType.DateTimePeriod(
                    id = PickupPointDetails.DeliveryType.DateTimePeriod.Id(id),
                    title = title,
                )
            }

            companion object {
                fun from(period: PickupPointDetails.DeliveryType.DateTimePeriod): DateTimePeriod {
                    return DateTimePeriod(
                        id = period.id.value,
                        title = period.title,
                    )
                }
            }
        }

        companion object {
            fun from(deliveryType: PickupPointDetails.DeliveryType): DeliveryType {
                return DeliveryType(
                    id = deliveryType.id.value,
                    title = deliveryType.title,
                    description = deliveryType.description,
                    dateTimePeriods = deliveryType.dateTimePeriods.map { DateTimePeriod.from(it) },
                )
            }
        }
    }

    companion object {
        fun from(pickupPoint: PickupPoint): PickupPointParcelable {
            val details = pickupPoint as? PickupPointDetails
            return PickupPointParcelable(
                id = pickupPoint.id.value,
                title = pickupPoint.title,
                address = pickupPoint.address,
                location = LocationParcelable.from(pickupPoint.location),
                isFittingAvailable = pickupPoint.isFittingAvailable,
                isPaymentByCardAvailable = pickupPoint.isPaymentByCardAvailable,
                availablePaymentMethods = pickupPoint.availablePaymentMethods,
                schedule = details?.schedule,
                expectedDeliveryDate = details?.expectedDeliveryDate,
                storageTime = details?.storageTime,
                deliveryTypes = details?.deliveryTypes?.map { DeliveryType.from(it) },
            )
        }
    }
}
