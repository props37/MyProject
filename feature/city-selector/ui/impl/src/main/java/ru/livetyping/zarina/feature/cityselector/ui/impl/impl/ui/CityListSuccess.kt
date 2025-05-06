package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent

@Composable
internal fun CityListSuccess(
    state: CityListState.Success,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    topPadding: Dp,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = topPadding,
            bottom = bottomPadding + ZarinaScrollableDefaults.ScrollableBottomPadding,
        ),
        modifier = modifier,
    ) {
        items(
            items = state.cities,
            key = { it.city.id.value },
        ) { item ->
            CityListItem(
                item = item,
                onClick = { onCitySelectorEvent(CitySelectorEvent.CitySelected(item.city)) },
            )
        }
    }
}
