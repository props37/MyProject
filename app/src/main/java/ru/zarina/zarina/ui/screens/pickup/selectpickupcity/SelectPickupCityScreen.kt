package ru.zarina.zarina.ui.screens.pickup.selectpickupcity

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
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
    goBack: () -> Unit,
) {
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)
    val viewModel = hiltViewModel<SelectPickupCityViewModel>()

    val query by viewModel.query.collectAsStateWithLifecycle()
    val cityItems by viewModel.cities.collectAsStateWithLifecycle()
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()
    val isSearchLoadingVisible by viewModel.isLoaderVisible.collectAsStateWithLifecycle()

    SelectPickupCityScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SelectCityScreenContent(
        isSearchLoadingVisible = isSearchLoadingVisible,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cityItems,
        onCityClick = {
            viewModel.onCityClick()
            parentViewModel.onCityClick(it)
        },
        isRegionVisible = false,
        errorType = errorType,
        onErrorButtonClick = { viewModel.onErrorButtonClick() },
        onCloseClick = viewModel::onCloseClick,
        isSnackbarVisible = false,
        snackbarText = Text.Empty,
    )
}

@Composable
fun SelectPickupCityScreenBehavior(
    sideEffects: Flow<SelectPickupCityViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    val context by rememberUpdatedState(LocalContext.current)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectPickupCityViewModel.SideEffect.GoBack -> goBack()
                is SelectPickupCityViewModel.SideEffect.ShowError -> Toast
                    .makeText(context, effect.message.getString(context), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
