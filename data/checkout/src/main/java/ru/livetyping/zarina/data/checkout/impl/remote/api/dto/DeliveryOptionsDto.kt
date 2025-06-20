package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import timber.log.Timber
import java.math.BigDecimal

@Serializable
internal data class DeliveryOptionsDto(
    @SerialName("trying_types")
    val tryingTypes: List<OptionDto>? = null,
) {
    fun toDeliveryOptions(type: DeliveryOptionsDtoType): List<DeliveryOption> {
        val firstDeliveryOption = tryingTypes?.firstOrNull()?.toDeliveryOption(type)
        checkNotNull(firstDeliveryOption)
        return listOf(firstDeliveryOption)
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
        fun toDeliveryOption(type: DeliveryOptionsDtoType): DeliveryOption? {
            val dateTimePeriods = periods?.mapNotNull { it.toDateTimePeriod(type) }
            return if (id != null && title != null && description != null && price != null && dateTimePeriods != null) {
                return DeliveryOption(
                    id = DeliveryOption.Id(id),
                    title = title,
                    description = description,
                    price = BigDecimal(price.toDouble()),
                    dateTimePeriods = dateTimePeriods,
                )
            } else {
                Timber.tag(TAG).e("Ignore $this because it can't be mapped to DeliveryOption")
                null
            }
        }

        @Serializable
        data class DateTimePeriodDto(
            @SerialName("id")
            val id: String? = null,

            @SerialName("title")
            val title: String? = null,
        ) {
            fun toDateTimePeriod(type: DeliveryOptionsDtoType): DeliveryOption.DateTimePeriod? {
                return if (id != null && title != null) {
                    val segments = title.split(DATE_TIME_PERIOD_SEPARATOR)
                    val date = when (type) {
                        DeliveryOptionsDtoType.COURIER -> {
                            val dayOfWeek = segments.getOrNull(0)
                            val dayOfMonth = segments.getOrNull(1)
                            "$dayOfWeek, $dayOfMonth"
                        }

                        DeliveryOptionsDtoType.POST -> title
                    }
                    val time = when (type) {
                        DeliveryOptionsDtoType.COURIER -> {
                            segments.getOrNull(2)
                        }

                        DeliveryOptionsDtoType.POST -> null
                    }
                    DeliveryOption.DateTimePeriod(
                        id = DeliveryOption.DateTimePeriod.Id(id),
                        date = date,
                        time = time,
                    )
                } else {
                    Timber.tag(TAG).e("Ignore $this because it can't be mapped to DateTimePeriod")
                    null
                }
            }

            companion object {
                private const val DATE_TIME_PERIOD_SEPARATOR = ", "
            }
        }
    }

    private companion object {
        private const val TAG = "DeliveryOptionsDto"
    }
}

internal enum class DeliveryOptionsDtoType { COURIER, POST }
