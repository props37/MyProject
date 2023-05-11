package ru.zarina.zarina.ui.common.tooling.preview.providers.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import ru.zarina.zarina.domain.AddressId
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.screens.selectcity.SelectCityViewModel

class CityListItemProvider : PreviewParameterProvider<List<SelectCityViewModel.CityListItem>> {
    override val values = sequenceOf(
        persistentListOf(
            SelectCityViewModel.CityListItem.Header("С"),
            SelectCityViewModel.CityListItem.Item(
                City(
                    AddressId("7800000000000"),
                    "Cанкт-Петербург",
                    "г. Санкт-Петербург"
                )
            ),
            SelectCityViewModel.CityListItem.Header("М"),
            SelectCityViewModel.CityListItem.Item(
                City(
                    AddressId("7700000000000"),
                    "Москва",
                    "г. Москва"
                )
            ),
        )
    )
}
