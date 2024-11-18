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
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState

@Composable
internal fun CitySelectorScreen(
    navActions: CitySelectorNavActions,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val citySelectorState by viewModel.citySelectorState.collectAsStateWithLifecycle()
    val cityListState by viewModel.cityListState.collectAsStateWithLifecycle()

    ScreenContent(
        citySelectorState = citySelectorState,
        cityListState = cityListState,
        onCitySelectorEvent = viewModel::onCitySelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    citySelectorState: CitySelectorState,
    cityListState: CityListState,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
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
            title = citySelectorState.title,
            onBackClicked = { onCitySelectorEvent(CitySelectorEvent.BackClicked) },
        )

        Column {
            CitySearchTextField(
                state = citySelectorState.citySearchTextFieldState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            CityList(
                cityListState = cityListState,
                onCityClicked = { onCitySelectorEvent(CitySelectorEvent.CityClicked(it)) },
                onChangeCityClicked = { onCitySelectorEvent(CitySelectorEvent.ChangeCityClicked) },
                onCityListErrorRefreshClicked = {
                    onCitySelectorEvent(CitySelectorEvent.ErrorRefreshClicked)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
