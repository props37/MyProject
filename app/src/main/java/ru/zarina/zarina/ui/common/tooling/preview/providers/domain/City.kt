package ru.zarina.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.old.AddressId
import ru.zarina.zarina.domain.old.City

class CityProvider : PreviewParameterProvider<City> {
    override val values = sequenceOf(
        City(AddressId("7800000000000"), "Cанкт-Петербург", "г. Санкт-Петербург"),
        City(AddressId("7700000000000"), "Москва", "г. Москва"),
    )
}

