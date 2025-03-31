package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDto.Companion.getAvailablePaymentMethods

@Serializable
internal data class PickupPointDetailedDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("location")
    val location: LocationDto? = null,

    @SerialName("is_trying_available")
    val isTryingAvailable: Boolean? = null,

    @SerialName("is_card_payment_available")
    val isCardPaymentAvailable: Boolean? = null,

    @SerialName("available_payments")
    val availablePayments: List<String>? = null,

    @SerialName("schedule")
    val schedule: String? = null,

    @SerialName("estimated_delivery")
    val estimatedDelivery: String? = null,

    @SerialName("shelf_life")
    val shelfTime: Int? = null,

    @SerialName("levels")
    val levels: List<DeliveryTypeDto>? = null,
) {
    fun toPickupPointDetails(): PickupPointDetailed {
        checkPropertyNotNull(id) { ::id }
        checkPropertyNotNull(title) { ::title }
        checkPropertyNotNull(address) { ::address }
        checkPropertyNotNull(location) { ::location }
        checkPropertyNotNull(schedule) { ::schedule }
        checkPropertyNotNull(estimatedDelivery) { ::estimatedDelivery }
        checkPropertyNotNull(availablePayments) { ::availablePayments }
        checkPropertyNotNull(shelfTime) { ::shelfTime }
        checkPropertyNotNull(levels) { ::levels }
        check(levels.isNotEmpty()) { "levels is empty" }
        return PickupPointDetailed(
            id = PickupPoint.Id(id.toString()),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isTryingAvailable ?: false,
            isPaymentByCardAvailable = isCardPaymentAvailable ?: false,
            availablePaymentMethods = getAvailablePaymentMethods(availablePayments),
            schedule = schedule,
            expectedDeliveryDate = estimatedDelivery,
            shelfTime = shelfTime,
            deliveryTypes = levels.map { it.toDeliveryType() },
        )
    }

    @Serializable
    data class DeliveryTypeDto(
        @SerialName("code")
        val code: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("description")
        val description: String? = null,

        @SerialName("intervals")
        val intervals: List<DateTimePeriodDto>? = null,
    ) {
        fun toDeliveryType(): PickupPointDetailed.DeliveryType {
            checkPropertyNotNull(code) { ::code }
            checkPropertyNotNull(name) { ::name }
            checkPropertyNotNull(description) { ::description }
            checkPropertyNotNull(intervals) { ::intervals }
            check(intervals.isNotEmpty()) { "intervals is empty" }
            return PickupPointDetailed.DeliveryType(
                id = PickupPointDetailed.DeliveryType.Id(code),
                title = name,
                description = description,
                dateTimePeriods = intervals.map { it.toDateTimePeriod() },
            )
        }

        @Serializable
        data class DateTimePeriodDto(
            @SerialName("id")
            val id: Long? = null,

            @SerialName("title")
            val title: String? = null,
        ) {
            fun toDateTimePeriod(): PickupPointDetailed.DeliveryType.DateTimePeriod {
                checkPropertyNotNull(id) { ::id }
                checkPropertyNotNull(title) { ::title }
                return PickupPointDetailed.DeliveryType.DateTimePeriod(
                    id = PickupPointDetailed.DeliveryType.DateTimePeriod.Id(id),
                    title = title,
                )
            }
        }
    }
}
