package ru.livetyping.zarina.ui.common.tooling.preview.providers.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import ru.livetyping.zarina.domain.old.AddressId
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.ui.screens.bases.selectcity.SelectCityComponent

class CityListItemProvider : PreviewParameterProvider<List<SelectCityComponent.CityListItem>> {
    override val values = sequenceOf(
        persistentListOf(
            SelectCityComponent.CityListItem.Header("С"),
            SelectCityComponent.CityListItem.Item(
                City(
                    AddressId("7800000000000"),
                    "Cанкт-Петербург",
                    "г. Санкт-Петербург"
                )
            ),
            SelectCityComponent.CityListItem.Header("М"),
            SelectCityComponent.CityListItem.Item(
                City(
                    AddressId("7700000000000"),
                    "Москва",
                    "г. Москва"
                )
            ),
        )
    )
}
