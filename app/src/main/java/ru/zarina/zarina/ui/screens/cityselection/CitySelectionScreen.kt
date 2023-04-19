package ru.zarina.zarina.ui.screens.cityselection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun CitySelectionScreenContent() {

}

@Composable
fun CitySelectionScreen() {
    val viewModel = hiltViewModel<CitySelectionViewModel>()

    CitySelectionScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    CitySelectionScreenContent()
}

@Composable
fun CitySelectionScreenBehavior(
    sideEffects: Flow<CitySelectionViewModel.SideEffect>,
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
fun CitySelectionScreenContentPreview() {
    ZarinaTheme {
        CitySelectionScreenContent()
    }
}
