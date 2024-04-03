package ru.livetyping.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.domain.old.AddressId
import ru.livetyping.zarina.domain.old.City

class CityProvider : PreviewParameterProvider<City> {
    override val values = sequenceOf(
        City(AddressId("7800000000000"), "Cанкт-Петербург", "г. Санкт-Петербург"),
        City(AddressId("7700000000000"), "Москва", "г. Москва"),
    )
}

