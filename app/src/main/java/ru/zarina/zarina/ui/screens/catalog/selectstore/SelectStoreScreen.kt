package ru.zarina.zarina.ui.screens.catalog.selectstore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectStoreScreenContent() {

}

@Composable
fun SelectStoreScreen() {
    val viewModel = koinViewModel<SelectStoreViewModel>()

    SelectStoreScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectStoreScreenContent()
}

@Composable
fun SelectStoreScreenBehavior(
    sideEffects: Flow<SelectStoreViewModel.SideEffect>,
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
fun SelectStoreScreenContentPreview() {
    ZarinaTheme {
        SelectStoreScreenContent()
    }
}

