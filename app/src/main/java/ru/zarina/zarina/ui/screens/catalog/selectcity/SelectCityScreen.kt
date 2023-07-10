package ru.zarina.zarina.ui.screens.catalog.selectcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectCityScreenContent() {

}

@Composable
fun SelectCityScreen(
    selectShopSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<SelectCityViewModel> { parametersOf(selectShopSavedStateHandle) }

    SelectCityScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SelectCityScreenContent()
}

@Composable
fun SelectCityScreenBehavior(
    sideEffects: Flow<SelectCityViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectCityViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectCityScreenContentPreview() {
    ZarinaTheme {
        SelectCityScreenContent()
    }
}

