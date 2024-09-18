package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.DeliveryOption

@Serializable
data class DeliveryOptionsDto(
    @SerialName("trying_types")
    val options: List<Option>? = null,
) {
    fun toDeliveryOptions(type: DeliveryOptionsDtoType): List<DeliveryOption> {
        checkNotNull(options) { "options is null" }
        return options.map { it.toDeliveryOption(type) }
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
        fun toDeliveryOption(type: DeliveryOptionsDtoType): DeliveryOption {
            checkNotNull(id) { "id is null" }
            checkNotNull(title) { "title is null" }
            checkNotNull(description) { "description is null" }
            checkNotNull(price) { "price is null" }
            checkNotNull(dateTimePeriods) { "periods is null" }
            val dateTimePeriods = dateTimePeriods.map { it.toDateTimePeriod(type) }
            return DeliveryOption(
                id = DeliveryOption.Id(id),
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
            fun toDateTimePeriod(
                type: DeliveryOptionsDtoType,
            ): DeliveryOption.DateTimePeriod {
                checkNotNull(id) { "id is null" }
                checkNotNull(text) { "text is null" }
                val date = when (type) {
                    DeliveryOptionsDtoType.COURIER -> {
                        text.substringBeforeLast(DATE_TIME_PERIOD_SEPARATOR)
                    }

                    DeliveryOptionsDtoType.POST -> text
                }
                val time = when (type) {
                    DeliveryOptionsDtoType.COURIER -> {
                        text.substringAfterLast(DATE_TIME_PERIOD_SEPARATOR)
                    }

                    DeliveryOptionsDtoType.POST -> null
                }
                return DeliveryOption.DateTimePeriod(
                    id = DeliveryOption.DateTimePeriod.Id(id),
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

enum class DeliveryOptionsDtoType { COURIER, POST }
