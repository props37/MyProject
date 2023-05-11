package ru.zarina.zarina.ui.screens.selectcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityScreenContent

@Composable
fun SelectCityScreen(
    showHome: () -> Unit,
) {
    val viewModel = hiltViewModel<SelectCityViewModel>()

    val isSearchLoadingVisible by viewModel.isSearchLoadingVisible.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val cityItems by viewModel.cities.collectAsStateWithLifecycle()
    val isRegionVisible by viewModel.isRegionVisible.collectAsStateWithLifecycle()
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()
    val isSnackbarVisible by viewModel.isSnackbarVisible.collectAsStateWithLifecycle()
    val snackbarText by viewModel.snackbarText.collectAsStateWithLifecycle()

    SelectCityScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showHome = showHome,
    )

    SelectCityScreenContent(
        isSearchLoadingVisible = isSearchLoadingVisible,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cityItems,
        onCityClick = viewModel::onCityClick,
        isRegionVisible = isRegionVisible,
        errorType = errorType,
        onErrorButtonClick = viewModel::onErrorButtonClick,
        onCloseClick = viewModel::onCloseClick,
        isSnackbarVisible = isSnackbarVisible,
        snackbarText = snackbarText,
    )
}

@Composable
fun SelectCityScreenBehavior(
    sideEffects: Flow<SelectCityViewModel.SideEffect>,
    showHome: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectCityViewModel.SideEffect.ShowHome -> showHome()
            }
        }
    }
}
