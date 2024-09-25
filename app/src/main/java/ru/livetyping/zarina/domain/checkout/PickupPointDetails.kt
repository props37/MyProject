package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.location.Location

data class PickupPointDetails(
    override val id: Id,
    override val title: String,
    override val address: String,
    override val location: Location,
    override val isFittingAvailable: Boolean,
    override val isPaymentByCardAvailable: Boolean,
    override val availablePaymentMethods: Set<PaymentMethod>,
    val schedule: String,
    val expectedDeliveryDate: String,
    val storageTime: Int,
    val deliveryTypes: List<DeliveryType>,
) : PickupPoint(
    id = id,
    title = title,
    address = address,
    location = location,
    isFittingAvailable = isFittingAvailable,
    isPaymentByCardAvailable = isPaymentByCardAvailable,
    availablePaymentMethods = availablePaymentMethods,
) {
    data class DeliveryType(
        val id: Id,
        val title: String,
        val description: String,
        val dateTimePeriods: List<DateTimePeriod>,
    ) {
        @JvmInline
        value class Id(val value: String)

        data class DateTimePeriod(
            val id: Id,
            val title: String,
        ) {
            @JvmInline
            value class Id(val value: Long)
        }
    }
}
