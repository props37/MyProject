package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun FiltersScreenContent() {

}

@Composable
fun FiltersScreen() {
    val viewModel = koinViewModel<FiltersViewModel>()

    FiltersScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    FiltersScreenContent()
}

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
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
fun FiltersScreenContentPreview() {
    ZarinaTheme {
        FiltersScreenContent()
    }
}
