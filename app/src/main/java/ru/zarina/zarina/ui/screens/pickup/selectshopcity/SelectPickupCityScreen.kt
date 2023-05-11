package ru.zarina.zarina.ui.screens.pickup.selectshopcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityScreenContent
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel

@Composable
fun SelectPickupCityScreen(
    parentEntry: NavBackStackEntry,
) {
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)
    val viewModel = hiltViewModel<SelectPickupCityViewModel>()

    val query by viewModel.query.collectAsStateWithLifecycle()
    val cityItems by viewModel.cities.collectAsStateWithLifecycle()
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()

    SelectPickupCityScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    SelectCityScreenContent(
        isSearchLoadingVisible = false,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cityItems,
        onCityClick = {
            viewModel.onCityClick(it)
            parentViewModel.onCityClick(it)
        },
        isRegionVisible = false,
        errorType = errorType,
        onErrorButtonClick = viewModel::onErrorButtonClick,
        onCloseClick = viewModel::onCloseClick,
        isSnackbarVisible = false,
        snackbarText = Text.Empty,
    )
}

@Composable
fun SelectPickupCityScreenBehavior(
    sideEffects: Flow<SelectPickupCityViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> Unit // TODO
            }
        }
    }
}
