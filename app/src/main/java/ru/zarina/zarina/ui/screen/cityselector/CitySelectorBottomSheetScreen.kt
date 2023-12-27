package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.CityList
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.CitySearchBar
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect

@Composable
fun CitySelectorBottomSheetScreen(
    navigateBackward: (CitySelectorScreenResult) -> Unit,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val cityNameQuery by viewModel.cityNameQuery.collectAsStateWithLifecycle()
    val cityListState by viewModel.cityListState.collectAsStateWithLifecycle()
    val isCitySearchBarVisible by viewModel.isCitySearchBarVisible.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val isChangeCityButtonVisible by viewModel.isChangeCityButtonVisible.collectAsStateWithLifecycle()

    ScreenContent(
        cityNameQuery = cityNameQuery,
        onCityNameQueryChanged = viewModel::onCityNameQueryChanged,
        onCitySearchBarClearClicked = viewModel::onCitySearchBarClearClicked,
        onCitySearchBarCancelClicked = viewModel::onCitySearchBarCancelClicked,
        cityListState = cityListState,
        isCitySearchBarVisible = isCitySearchBarVisible,
        selectedCity = selectedCity,
        onCityClicked = viewModel::onCityClicked,
        isChangeCityButtonVisible = isChangeCityButtonVisible,
        onChangeCityClicked = viewModel::onChangeCityClicked,
        onErrorRefreshClicked = viewModel::onErrorRefreshClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    cityNameQuery: String,
    onCityNameQueryChanged: (String) -> Unit,
    onCitySearchBarClearClicked: () -> Unit,
    onCitySearchBarCancelClicked: () -> Unit,
    cityListState: CityListState,
    isCitySearchBarVisible: Boolean,
    selectedCity: City?,
    onCityClicked: (City) -> Unit,
    isChangeCityButtonVisible: Boolean,
    onChangeCityClicked: () -> Unit,
    onErrorRefreshClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (CitySelectorScreenResult) -> Unit,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    ZarinaBottomSheet(windowInsets = WindowInsets.statusBars) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onCloseClicked = onCloseClicked)

            AnimatedVisibility(
                visible = isCitySearchBarVisible,
                enter = remember {
                    fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90))
                },
                exit = remember {
                    fadeOut(animationSpec = tween(durationMillis = 90))
                },
            ) {
                CitySearchBar(
                    cityNameQuery = cityNameQuery,
                    onCityNameQueryChanged = onCityNameQueryChanged,
                    onClearClicked = onCitySearchBarClearClicked,
                    onCancelClicked = onCitySearchBarCancelClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            CityList(
                listState = cityListState,
                selectedCity = selectedCity,
                onCityClicked = onCityClicked,
                isChangeCityButtonVisible = isChangeCityButtonVisible,
                onChangeCityClicked = onChangeCityClicked,
                onErrorRefreshClicked = onErrorRefreshClicked,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}
