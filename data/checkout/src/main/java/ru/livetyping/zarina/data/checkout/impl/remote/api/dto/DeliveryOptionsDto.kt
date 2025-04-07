package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import java.math.BigDecimal

@Serializable
internal data class DeliveryOptionsDto(
    @SerialName("trying_types")
    val tryingTypes: List<OptionDto>? = null,
) {
    fun toDeliveryOptions(type: DeliveryOptionsDtoType): List<DeliveryOption> {
        checkPropertyNotNull(tryingTypes) { ::tryingTypes }
        return tryingTypes.map { it.toDeliveryOption(type) }
    }

    @Serializable
    data class OptionDto(
        @SerialName("id") 
        val id: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("description")
        val description: String? = null,

        @SerialName("price")
        val price: Int? = null,

        @SerialName("periods")
        val periods: List<DateTimePeriodDto>? = null,
    ) {
        fun toDeliveryOption(type: DeliveryOptionsDtoType): DeliveryOption {
            checkPropertyNotNull(id) { ::id }
            checkPropertyNotNull(title) { ::title }
            checkPropertyNotNull(description) { ::description }
            checkPropertyNotNull(price) { ::price }
            checkPropertyNotNull(periods) { ::periods }
            val dateTimePeriods = periods.map { it.toDateTimePeriod(type) }
            return DeliveryOption(
                id = DeliveryOption.Id(id),
                title = title,
                description = description,
                price = BigDecimal(price.toDouble()),
                dateTimePeriods = dateTimePeriods,
            )
        }

        @Serializable
        data class DateTimePeriodDto(
            @SerialName("id")
            val id: Long? = null,

            @SerialName("title")
            val title: String? = null,
        ) {
            fun toDateTimePeriod(type: DeliveryOptionsDtoType): DeliveryOption.DateTimePeriod {
                checkPropertyNotNull(id) { ::id }
                checkPropertyNotNull(title) { ::title }
                val date = when (type) {
                    DeliveryOptionsDtoType.COURIER -> {
                        title.substringBeforeLast(DATE_TIME_PERIOD_SEPARATOR)
                    }

                    DeliveryOptionsDtoType.POST -> title
                }
                val time = when (type) {
                    DeliveryOptionsDtoType.COURIER -> {
                        title.substringAfterLast(DATE_TIME_PERIOD_SEPARATOR)
                    }

                    DeliveryOptionsDtoType.POST -> null
                }
                return DeliveryOption.DateTimePeriod(
                    id = DeliveryOption.DateTimePeriod.Id(id.toString()),
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

internal enum class DeliveryOptionsDtoType { COURIER, POST }
