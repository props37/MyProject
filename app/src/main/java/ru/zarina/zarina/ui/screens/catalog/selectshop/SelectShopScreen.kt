package ru.zarina.zarina.ui.screens.catalog.selectshop

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
fun SelectShopScreenContent() {

}

@Composable
fun SelectShopScreen(
    filtersSavedStateHandle: SavedStateHandle,
    goBack: () -> Boolean
) {
    val viewModel = koinViewModel<SelectShopViewModel> { parametersOf(filtersSavedStateHandle) }

    SelectShopScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectShopScreenContent()
}

@Composable
fun SelectShopScreenBehavior(
    sideEffects: Flow<SelectShopViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectShopScreenContentPreview() {
    ZarinaTheme {
        SelectShopScreenContent()
    }
}

