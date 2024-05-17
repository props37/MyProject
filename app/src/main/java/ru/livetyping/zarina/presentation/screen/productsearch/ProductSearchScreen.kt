package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SideEffect

@Composable
fun ProductSearchScreen(
    navigate: (ProductSearchScreenAction) -> Unit,
    viewModel: ProductSearchViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSearchScreenAction) -> Unit,
) {
    ProductSearchScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )


}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
