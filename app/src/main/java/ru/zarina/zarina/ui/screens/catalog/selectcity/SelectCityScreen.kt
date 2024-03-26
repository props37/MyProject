package ru.zarina.zarina.ui.screens.catalog.selectcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityScreenContent

@Composable
fun SelectCityScreen(
    selectShopSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<SelectCityViewModel> { parametersOf(selectShopSavedStateHandle) }

    val query by viewModel.query.collectAsStateWithLifecycle()
    val cities by viewModel.cities.collectAsStateWithLifecycle()
    val isLoaderVisible by viewModel.isLoaderVisible.collectAsStateWithLifecycle()
    val isApplyButtonVisible by viewModel.isApplyButtonVisible.collectAsStateWithLifecycle()

    SelectCityScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SelectCityScreenContent(
        isSearchLoadingVisible = isLoaderVisible,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cities,
        onCityClick = viewModel::onCityClick,
        isRegionVisible = false,
        errorType = null,
        onErrorButtonClick = {},
        onCloseClick = viewModel::onBackClick,
        isSnackbarVisible = false,
        snackbarText = Text.Empty,
        isApplyButtonVisible = isApplyButtonVisible,
        onApplyButtonClick = viewModel::onApplyClick,
        isAutoscrollEnabled = false,
    )
}

@Composable
fun SelectCityScreenBehavior(
    sideEffects: Flow<SelectCityViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectCityViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}
