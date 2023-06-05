package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectSortScreenContent() {

}

@Composable
fun SelectSortScreen() {
    val viewModel = hiltViewModel<SelectSortViewModel>()

    SelectSortScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectSortScreenContent()
}

@Composable
fun SelectSortScreenBehavior(
    sideEffects: Flow<SelectSortViewModel.SideEffect>,
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
fun SelectSortScreenContentPreview() {
    ZarinaTheme {
        SelectSortScreenContent()
    }
}

