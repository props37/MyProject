package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Location

// Marked as stable on config/compose/stability_config.txt
public data class PickupPointDetailed(
    override val id: Id,
    override val title: String,
    override val address: String,
    override val location: Location,
    override val isFittingAvailable: Boolean,
    override val isPaymentByCardAvailable: Boolean,
    override val availablePaymentMethods: Set<PaymentMethod>,
    val schedule: String,
    val expectedDeliveryDate: String,
    val shelfTime: Int,
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
    // Marked as stable on config/compose/stability_config.txt
    public data class DeliveryType(
        val id: Id,
        val title: String,
        val description: String,
        val dateTimePeriods: List<DateTimePeriod>,
    ) {
        // Marked as stable on config/compose/stability_config.txt
        @JvmInline
        public value class Id(public val value: String)

        // Marked as stable on config/compose/stability_config.txt
        public data class DateTimePeriod(
            val id: Id,
            val title: String,
        ) {
            // Marked as stable on config/compose/stability_config.txt
            @JvmInline
            public value class Id(public val value: Long)
        }
    }
}
