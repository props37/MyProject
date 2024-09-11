package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDto.Companion.getAvailablePaymentMethods
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.LocationDto
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails

@Serializable
data class PickupPointDetailsDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("location")
    val location: LocationDto? = null,

    @SerialName("is_trying_available")
    val isFittingAvailable: Boolean? = null,

    @SerialName("is_card_payment_available")
    val isPaymentByCardAvailable: Boolean? = null,

    @SerialName("available_payments")
    val availablePaymentMethods: List<String>? = null,

    @SerialName("schedule")
    val schedule: String? = null,

    @SerialName("estimated_delivery")
    val expectedDeliveryDate: String? = null,

    @SerialName("shelf_life")
    val storageTime: Int? = null,

    @SerialName("levels")
    val deliveryTypes: List<DeliveryType>? = null,
) {
    fun toPickupPointDetails(): PickupPointDetails {
        checkNotNull(id) { "id is null" }
        checkNotNull(title) { "title is null" }
        checkNotNull(address) { "address is null" }
        checkNotNull(location) { "location is null" }
        checkNotNull(schedule) { "schedule is null" }
        checkNotNull(expectedDeliveryDate) { "expectedDeliveryDate is null" }
        checkNotNull(availablePaymentMethods) { "availablePaymentMethods is null" }
        checkNotNull(storageTime) { "storageTime is null" }
        checkNotNull(deliveryTypes) { "deliveryTypes is null" }
        check(deliveryTypes.isNotEmpty()) { "deliveryTypes is empty" }
        return PickupPointDetails(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable ?: false,
            isPaymentByCardAvailable = isPaymentByCardAvailable ?: false,
            availablePaymentMethods = getAvailablePaymentMethods(availablePaymentMethods),
            schedule = schedule,
            expectedDeliveryDate = expectedDeliveryDate,
            storageTime = storageTime,
            deliveryTypes = deliveryTypes.map { it.toDeliveryType() },
        )
    }

    @Serializable
    data class DeliveryType(
        @SerialName("code")
        val id: String? = null,

        @SerialName("name")
        val title: String? = null,

        @SerialName("description")
        val description: String? = null,

        @SerialName("intervals")
        val dateTimePeriods: List<DateTimePeriod>? = null,
    ) {
        fun toDeliveryType(): PickupPointDetails.DeliveryType {
            checkNotNull(id) { "id is null" }
            checkNotNull(title) { "title is null" }
            checkNotNull(description) { "description is null" }
            checkNotNull(dateTimePeriods) { "dateTimePeriods is null" }
            check(dateTimePeriods.isNotEmpty()) { "dateTimePeriods is empty" }
            return PickupPointDetails.DeliveryType(
                id = PickupPointDetails.DeliveryType.Id(id),
                title = title,
                description = description,
                dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod() },
            )
        }

        @Serializable
        data class DateTimePeriod(
            @SerialName("id")
            val id: Long? = null,

            @SerialName("title")
            val title: String? = null,
        ) {
            fun toDateTimePeriod(): PickupPointDetails.DeliveryType.DateTimePeriod {
                checkNotNull(id) { "id is null" }
                checkNotNull(title) { "title is null" }
                return PickupPointDetails.DeliveryType.DateTimePeriod(
                    id = PickupPointDetails.DeliveryType.DateTimePeriod.Id(id),
                    title = title,
                )
            }
        }
    }
}
