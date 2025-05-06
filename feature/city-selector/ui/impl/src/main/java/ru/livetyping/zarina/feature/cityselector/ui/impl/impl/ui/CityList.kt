package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen2
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent

@Suppress("NAME_SHADOWING")
@Composable
internal fun CityList(
    state: CityListState,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    topPadding: Dp,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is CityListState.Success -> ContentKey.Success
                CityListState.CityNotFound -> it
                CityListState.Loading -> it
                is CityListState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is CityListState.Success -> {
                CityListSuccess(
                    state = state,
                    onCitySelectorEvent = onCitySelectorEvent,
                    topPadding = topPadding,
                    bottomPadding = bottomPadding,
                )
            }

            CityListState.CityNotFound -> {
                CityListStateCityNotFound(
                    topPadding = topPadding,
                    bottomPadding = bottomPadding,
                )
            }

            CityListState.Loading -> {
                CityListLoading(topPadding, bottomPadding)
            }

            is CityListState.Error -> {
                ZarinaErrorScreen2(
                    state = state.state,
                    onButtonClick = { onCitySelectorEvent(CitySelectorEvent.RefreshClicked) },
                    bottomPadding = bottomPadding,
                )
            }
        }
    }
}

private enum class ContentKey { Success }
