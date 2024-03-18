package ru.zarina.zarina.ui.screen.cityselector.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.toImmutableList
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.domain.geography.KladrId
import ru.zarina.zarina.ui.common.error.ErrorState
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState

class CityListStatePreviewParameterProvider : PreviewParameterProvider<CityListState> {
    override val values: Sequence<CityListState>
        get() = sequenceOf(
            CityListState.CityList(getCityListItems().toImmutableList()),
            CityListState.Loading,
            CityListState.Error(ErrorState.NETWORK),
        )

    private fun getCityListItems(): List<CityListItem> {
        val saintPetersburg = City(
            name = "Санкт-Петербург",
            fullName = "Санкт-Петербург",
            region = "Санкт-Петербург",
            kladrId = KladrId("0"),
        )
        val moscow = City(
            name = "Москва",
            fullName = "Москва",
            region = "Москва",
            kladrId = KladrId("1"),
        )
        val anapa = City(
            name = "Анапа",
            fullName = "Анапа",
            region = "Анапа",
            kladrId = KladrId("2"),
        )
        return listOf(
            CityListItem.CityItem(saintPetersburg),
            CityListItem.CityItem(moscow),
            CityListItem.CityFirstLetterHeaderItem('А'),
            CityListItem.CityItem(anapa),
        )
    }
}
