package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun CityListSuccess(
    state: CityListState.Success,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    topPadding: Dp,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val lazyListState = rememberLazyListState()

        val selectCityButtonHeight = if (state.isSelectCityButtonVisible) {
            ZarinaButtonDefaults.SizeMedium + SelectCityBottomPadding
        } else {
            0.dp
        }
        val listBottomPadding = bottomPaddingProvider() +
                ZarinaScrollableDefaults.ScrollableBottomPadding + selectCityButtonHeight

        DisposableEffect(state.cities) {
            lazyListState.requestScrollToItem(0)
            onDispose {}
        }

        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(top = topPadding, bottom = listBottomPadding),
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

        AnimatedVisibility(
            visible = state.isSelectCityButtonVisible,
            enter = AnimatedContentDefaultEnterTransition,
            exit = AnimatedContentDefaultExitTransition,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomPaddingProvider() + SelectCityBottomPadding),
        ) {
            ZarinaButton(
                onClick = { onCitySelectorEvent(CitySelectorEvent.SelectCityClicked) },
                isLoading = state.isSelectCityButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HorizontalPadding),
            ) {
                Text(text = stringResource(RCommon.string.res_select).uppercase())
            }
        }
    }
}

private val SelectCityBottomPadding: Dp get() = 20.dp
