package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow

@Composable
internal fun ProductScreen(
    navActions: ProductNavActions,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<ProductSideEffect>,
    navActions: ProductNavActions,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )
}
