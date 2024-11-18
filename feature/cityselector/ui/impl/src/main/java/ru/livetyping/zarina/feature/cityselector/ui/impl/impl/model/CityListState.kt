package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class CityListState {
    @Immutable
    data class Success(
        val items: ImmutableList<CityListItem>,
        val selectedCity: City?,
        val isChangeCityButtonVisible: Boolean,
    ) : CityListState()

    data object Empty : CityListState()

    data object Loading : CityListState()

    @Immutable
    data class Error(
        val state: ZarinaErrorScreenState,
    ) : CityListState()
}
