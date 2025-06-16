package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui.ProductContent

@Composable
internal fun ProductScreen(
    navActions: ProductNavActions,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    LifecycleEventEffect(onLifecycleEvent = viewModel::onLifecycleEvent)

    val productState by viewModel.productState.collectAsStateWithLifecycle()

    ScreenContent(
        productState = productState,
        onProductEvent = viewModel::onProductEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    productState: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    sideEffects: Flow<ProductSideEffect>,
    navActions: ProductNavActions,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    ProductContent(
        state = productState,
        onEvent = onProductEvent,
        windowInsetsProvider = { WindowInsets.safeDrawing },
        bottomPaddingProvider = { bottomNavBarHeightAsState().value },
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white),
    )
}
