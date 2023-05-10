package ru.zarina.zarina.ui.screens.selectsize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectSizeScreenContent() {

}

@Composable
fun SelectSizeScreen() {
    val viewModel = hiltViewModel<SelectSizeViewModel>()

    SelectSizeScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectSizeScreenContent()
}

@Composable
fun SelectSizeScreenBehavior(
    sideEffects: Flow<SelectSizeViewModel.SideEffect>,
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
fun SelectSizeScreenContentPreview() {
    ZarinaTheme {
        SelectSizeScreenContent()
    }
}
