package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.uimodel.common.LocationParcelable

@Serializable
@Parcelize
public data class PickupPointParcelable(
    val id: String,
    val title: String,
    val address: String,
    val location: LocationParcelable,
    val isFittingAvailable: Boolean,
    val isPaymentByCardAvailable: Boolean,
    val availablePaymentMethods: Set<PickupPoint.PaymentMethod>,
    val schedule: String?,
    val expectedDeliveryDate: String?,
    val shelfTimeInDays: Int?,
    val deliveryTypes: List<DeliveryTypeParcelable>?,
) : Parcelable {
    public fun toPickupPointShort(): PickupPointShort {
        return PickupPointShort(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable,
            isPaymentByCardAvailable = isPaymentByCardAvailable,
            availablePaymentMethods = availablePaymentMethods,
        )
    }

    public fun toPickupPointDetailed(): PickupPointDetailed {
        if (
            schedule == null || expectedDeliveryDate == null
            || shelfTimeInDays == null || deliveryTypes == null
        ) {
            error("Can not cast $this to PickupPointDetailed because some properties are null")
        }

        return PickupPointDetailed(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable,
            isPaymentByCardAvailable = isPaymentByCardAvailable,
            availablePaymentMethods = availablePaymentMethods,
            schedule = schedule,
            expectedDeliveryDate = expectedDeliveryDate,
            shelfTimeInDays = shelfTimeInDays,
            deliveryTypes = deliveryTypes.map { it.toDeliveryType() },
        )
    }

    @Serializable
    @Parcelize
    public data class DeliveryTypeParcelable(
        val id: String,
        val title: String,
        val description: String,
        val dateTimePeriods: List<DateTimePeriodParcelable>,
    ) : Parcelable {
        public fun toDeliveryType(): PickupPointDetailed.DeliveryType {
            return PickupPointDetailed.DeliveryType(
                id = PickupPointDetailed.DeliveryType.Id(id),
                title = title,
                description = description,
                dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod() },
            )
        }

        @Serializable
        @Parcelize
        public data class DateTimePeriodParcelable(
            val id: String,
            val title: String,
        ) : Parcelable {
            public fun toDateTimePeriod(): PickupPointDetailed.DeliveryType.DateTimePeriod {
                return PickupPointDetailed.DeliveryType.DateTimePeriod(
                    id = PickupPointDetailed.DeliveryType.DateTimePeriod.Id(id),
                    title = title,
                )
            }

            public companion object {
                public fun from(
                    period: PickupPointDetailed.DeliveryType.DateTimePeriod,
                ): DateTimePeriodParcelable {
                    return DateTimePeriodParcelable(
                        id = period.id.value,
                        title = period.title,
                    )
                }
            }
        }

        public companion object {
            public fun from(deliveryType: PickupPointDetailed.DeliveryType): DeliveryTypeParcelable {
                return DeliveryTypeParcelable(
                    id = deliveryType.id.value,
                    title = deliveryType.title,
                    description = deliveryType.description,
                    dateTimePeriods = deliveryType.dateTimePeriods.map {
                        DateTimePeriodParcelable.from(it)
                    },
                )
            }
        }
    }

    public companion object {
        public fun from(pickupPoint: PickupPoint): PickupPointParcelable {
            val detailed = pickupPoint as? PickupPointDetailed
            return PickupPointParcelable(
                id = pickupPoint.id.value,
                title = pickupPoint.title,
                address = pickupPoint.address,
                location = LocationParcelable.from(pickupPoint.location),
                isFittingAvailable = pickupPoint.isFittingAvailable,
                isPaymentByCardAvailable = pickupPoint.isPaymentByCardAvailable,
                availablePaymentMethods = pickupPoint.availablePaymentMethods,
                schedule = detailed?.schedule,
                expectedDeliveryDate = detailed?.expectedDeliveryDate,
                shelfTimeInDays = detailed?.shelfTimeInDays,
                deliveryTypes = detailed?.deliveryTypes?.map { DeliveryTypeParcelable.from(it) },
            )
        }
    }
}