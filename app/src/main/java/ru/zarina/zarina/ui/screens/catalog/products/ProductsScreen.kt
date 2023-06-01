package ru.zarina.zarina.ui.screens.catalog.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductsScreenContent() {

}

@Composable
fun ProductsScreen() {
    val viewModel = hiltViewModel<ProductsViewModel>()

    ProductsScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    ProductsScreenContent()
}

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<ProductsViewModel.SideEffect>,
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
fun ProductsScreenContentPreview() {
    ZarinaTheme {
        ProductsScreenContent()
    }
}
