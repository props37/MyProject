package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.DeliveryOptions

@Serializable
data class DeliveryOptionsDto(
    @SerialName("trying_types")
    val options: List<Option>? = null,
) {
    fun toDeliveryOptions(): DeliveryOptions {
        checkNotNull(options) { "options is null" }
        val options = options.map { it.toOption() }
        return DeliveryOptions(
            options = options,
        )
    }

    @Serializable
    data class Option(
        @SerialName("id") 
        val id: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("description")
        val description: String? = null,

        @SerialName("price")
        val price: Int? = null,

        @SerialName("periods")
        val dateTimePeriods: List<DateTimePeriod>? = null,
    ) {
        fun toOption(): DeliveryOptions.Option {
            checkNotNull(id) { "id is null" }
            checkNotNull(title) { "title is null" }
            checkNotNull(description) { "description is null" }
            checkNotNull(price) { "price is null" }
            checkNotNull(dateTimePeriods) { "periods is null" }
            val dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod() }
            return DeliveryOptions.Option(
                id = DeliveryOptions.Option.Id(id),
                title = title,
                description = description,
                price = price,
                dateTimePeriods = dateTimePeriods,
            )
        }

        @Serializable
        data class DateTimePeriod(
            @SerialName("id")
            val id: Long? = null,

            @SerialName("title")
            val text: String? = null,
        ) {
            fun toDateTimePeriod(): DeliveryOptions.Option.DateTimePeriod {
                checkNotNull(id) { "id is null" }
                checkNotNull(text) { "text is null" }
                val date = text.substringBeforeLast(DATE_TIME_PERIOD_SEPARATOR)
                val time = text.substringAfterLast(DATE_TIME_PERIOD_SEPARATOR)
                return DeliveryOptions.Option.DateTimePeriod(
                    id = DeliveryOptions.Option.DateTimePeriod.Id(id),
                    date = date,
                    time = time,
                )
            }

            companion object {
                private const val DATE_TIME_PERIOD_SEPARATOR = ", "
            }
        }
    }
}
