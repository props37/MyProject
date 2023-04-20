package ru.zarina.zarina.ui.screens.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductScreenContent() {

}

@Composable
fun ProductScreen() {
    val viewModel = hiltViewModel<ProductViewModel>()

    ProductScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    ProductScreenContent()
}

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<ProductViewModel.SideEffect>,
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
fun ProductScreenContentPreview() {
    ZarinaTheme {
        ProductScreenContent()
    }
}
