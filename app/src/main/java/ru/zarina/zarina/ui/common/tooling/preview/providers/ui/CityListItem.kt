package ru.zarina.zarina.ui.common.tooling.preview.providers.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import ru.zarina.zarina.domain.AddressId
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.screens.cityselection.CitySelectionViewModel

class CityListItemProvider : PreviewParameterProvider<List<CitySelectionViewModel.CityListItem>> {
    override val values = sequenceOf(
        persistentListOf(
            CitySelectionViewModel.CityListItem.Header("С"),
            CitySelectionViewModel.CityListItem.Item(
                City(
                    AddressId("7800000000000"),
                    "Cанкт-Петербург",
                    "г. Санкт-Петербург"
                )
            ),
            CitySelectionViewModel.CityListItem.Header("М"),
            CitySelectionViewModel.CityListItem.Item(
                City(
                    AddressId("7700000000000"),
                    "Москва",
                    "г. Москва"
                )
            ),
        )
    )
}
