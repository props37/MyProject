package ru.livetyping.zarina.feature.cityselector.ui.impl.screen.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen2
import ru.livetyping.zarina.feature.cityselector.ui.impl.screen.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.screen.model.CitySelectorEvent

@Suppress("NAME_SHADOWING")
@Composable
internal fun CityList(
    state: CityListState,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    topPadding: Dp,
    bottomPaddingProvider: @Composable () -> Dp,
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
                    bottomPaddingProvider = bottomPaddingProvider,
                )
            }

            CityListState.CityNotFound -> {
                CityListStateCityNotFound(
                    topPadding = topPadding,
                    bottomPaddingProvider = bottomPaddingProvider,
                )
            }

            CityListState.Loading -> {
                CityListLoading(topPadding, bottomPaddingProvider)
            }

            is CityListState.Error -> {
                ZarinaErrorScreen2(
                    state = state.state,
                    onButtonClick = { onCitySelectorEvent(CitySelectorEvent.RefreshClicked) },
                    bottomPaddingProvider = bottomPaddingProvider,
                )
            }
        }
    }
}

private enum class ContentKey { Success }
