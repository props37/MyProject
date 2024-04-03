package ru.livetyping.zarina.ui.screens.pickup.selectpickupcity

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.livetyping.zarina.ui.screens.bases.selectcity.SelectCityScreenContent
import ru.livetyping.zarina.ui.screens.pickup.PickupViewModel

@Composable
fun SelectPickupCityScreen(
    parentEntry: NavBackStackEntry,
    goBack: () -> Unit,
) {
    val parentViewModel = koinViewModel<PickupViewModel>(viewModelStoreOwner = parentEntry)
    val viewModel = koinViewModel<SelectPickupCityViewModel>()

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
    NavigationBarState(isVisible = false, isAnimated = false)
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
