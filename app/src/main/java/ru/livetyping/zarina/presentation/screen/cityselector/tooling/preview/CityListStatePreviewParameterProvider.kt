package ru.livetyping.zarina.presentation.screen.cityselector.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.CityListState

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
            id = KladrId("0"),
            fullName = "Санкт-Петербург",
            region = "Санкт-Петербург",
        )
        val moscow = City(
            name = "Москва",
            id = KladrId("1"),
            fullName = "Москва",
            region = "Москва",
        )
        val anapa = City(
            name = "Анапа",
            id = KladrId("2"),
            fullName = "Анапа",
            region = "Анапа",
        )
        return listOf(
            CityListItem.CityItem(saintPetersburg),
            CityListItem.CityItem(moscow),
            CityListItem.CityFirstLetterHeaderItem('А'),
            CityListItem.CityItem(anapa),
        )
    }
}
