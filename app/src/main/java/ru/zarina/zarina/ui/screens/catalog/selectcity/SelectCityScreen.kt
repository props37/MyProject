package ru.zarina.zarina.ui.screens.catalog.selectcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectCityScreenContent() {

}

@Composable
fun SelectCityScreen() {
    val viewModel = koinViewModel<SelectCityViewModel>()

    SelectCityScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectCityScreenContent()
}

@Composable
fun SelectCityScreenBehavior(
    sideEffects: Flow<SelectCityViewModel.SideEffect>,
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
fun SelectCityScreenContentPreview() {
    ZarinaTheme {
        SelectCityScreenContent()
    }
}

