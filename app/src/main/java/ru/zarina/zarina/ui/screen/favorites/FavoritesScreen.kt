package ru.zarina.zarina.ui.screen.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.favorites.FavoritesScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    ScreenContent(
        productPagingDataFlow = viewModel.productPagingDataFlow,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    productPagingDataFlow: Flow<PagingData<Product>>,
    sideEffects: Flow<SideEffect>,
) {
    FavoritesScreenBehavior(sideEffects = sideEffects)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            isClearButtonVisible = true, // TODO: [High] Implement
            onClearClicked = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
