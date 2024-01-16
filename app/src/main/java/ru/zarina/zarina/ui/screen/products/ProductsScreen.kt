package ru.zarina.zarina.ui.screen.products

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.base.ScreenPlaceholder
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<ProductsViewModel.SideEffect>,
) {
    ProductsScreenBehavior(sideEffects = sideEffects)

    ScreenPlaceholder("Products")
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
