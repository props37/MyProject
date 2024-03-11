package ru.zarina.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import ru.zarina.zarina.domain.old.DeliveryAvailability

class DeliveryAvailabilityProvider : PreviewParameterProvider<DeliveryAvailability> {
    override val values = sequenceOf(
        DeliveryAvailability(
            cityName = "Санкт-Петербург",
            options = persistentListOf(
                DeliveryAvailability.Option(
                    type = DeliveryAvailability.Option.Type.EXPRESS,
                    name = "Курьерская доставка",
                    estimatedTimeDays = 1
                ),
                DeliveryAvailability.Option(
                    type = DeliveryAvailability.Option.Type.POST,
                    name = "Почта России",
                    estimatedTimeDays = 3
                ),
                DeliveryAvailability.Option(
                    type = DeliveryAvailability.Option.Type.PICKUP,
                    name = "Пункт самовывоза",
                    estimatedTimeDays = 2
                ),
                DeliveryAvailability.Option(
                    type = DeliveryAvailability.Option.Type.RETAIL,
                    name = "Забрать из магазина",
                    estimatedTimeDays = 0
                ),
            )
        )
    )
}
