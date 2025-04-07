package ru.livetyping.zarina.core.domain.model.checkout

import java.math.BigDecimal

// Marked as stable on config/compose/stability_config.txt
public data class DeliveryOption(
    val id: Id,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val dateTimePeriods: List<DateTimePeriod>,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    public data class DateTimePeriod(
        val id: Id,
        val date: String,
        val time: String?,
    ) {
        // Marked as stable on config/compose/stability_config.txt
        @JvmInline
        public value class Id(public val value: String)
    }
}
