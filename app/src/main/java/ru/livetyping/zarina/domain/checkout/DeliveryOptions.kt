package ru.livetyping.zarina.domain.checkout

data class DeliveryOptions(
    val options: List<Option>,
) {
    data class Option(
        val id: Id,
        val title: String,
        val description: String,
        val price: Int,
        val dateTimePeriods: List<DateTimePeriod>,
    ) {
        @JvmInline
        value class Id(val value: String)

        data class DateTimePeriod(
            val id: Id,
            val date: String,
            val time: String?,
        ) {
            @JvmInline
            value class Id(val value: Long)
        }
    }
}
