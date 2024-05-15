package ru.livetyping.zarina.presentation.screen.cityselector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorScreenComponents.CityList
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorScreenComponents.CitySearchBar
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.CityListState
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.cityselector.tooling.preview.CityListStatePreviewParameterProvider
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CitySelectorScreen(
    navigate: (CitySelectorScreenAction) -> Unit,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val cityNameQuery by viewModel.cityNameQuery.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val cityListState by viewModel.cityListState.collectAsStateWithLifecycle()
    val isCitySearchBarVisible by viewModel.isCitySearchBarVisible.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val isChangeCityButtonVisible by viewModel.isChangeCityButtonVisible.collectAsStateWithLifecycle()

    ScreenContent(
        title = title,
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
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    title: Text,
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
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CitySelectorScreenAction) -> Unit,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .statusBarsPadding()
            .displayCutoutPadding(),
    ) {
        TopBar(
            title = title,
            onBackClicked = onBackClicked,
        )

        AnimatedVisibility(
            visible = isCitySearchBarVisible,
            enter = remember { fadeIn(tween()) },
            exit = remember { fadeOut(tween()) },
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview(
    @PreviewParameter(CityListStatePreviewParameterProvider::class)
    cityListState: CityListState,
) {
    ZarinaPreview {
        ScreenContent(
            title = remember { Text.Resource(R.string.city) },
            cityNameQuery = "",
            onCityNameQueryChanged = {},
            onCitySearchBarClearClicked = {},
            onCitySearchBarCancelClicked = {},
            cityListState = cityListState,
            isCitySearchBarVisible = cityListState is CityListState.CityList,
            selectedCity = remember { City.DEFAULT },
            onCityClicked = {},
            isChangeCityButtonVisible = false,
            onChangeCityClicked = {},
            onErrorRefreshClicked = {},
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
