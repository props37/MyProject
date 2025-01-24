package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component.CityList
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component.CitySearchTextField
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.TopBarState

@Composable
internal fun CitySelectorScreen(
    navActions: CitySelectorNavActions,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val cityListState by viewModel.cityListState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        cityListState = cityListState,
        onCityListEvent = viewModel::onCitySelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    cityListState: CityListState,
    onCityListEvent: (CityListEvent) -> Unit,
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorNavActions,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(
            title = topBarState.title,
            onBackClicked = { onTopBarEvent(TopBarEvent.BackClicked) },
        )

        Column {
            CitySearchTextField(
                state = topBarState.citySearchTextFieldState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            CityList(
                cityListState = cityListState,
                onCityListEvent = onCityListEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
