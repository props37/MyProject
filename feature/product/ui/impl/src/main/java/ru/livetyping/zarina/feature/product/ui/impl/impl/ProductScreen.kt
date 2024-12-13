package ru.livetyping.zarina.feature.product.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.api.ProductNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.component.topBarModeAsState
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarMode
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarState

@Composable
internal fun ProductScreen(
    navActions: ProductNavActions,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val productState by viewModel.productState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        productState = productState,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    productState: ProductState,
    sideEffects: Flow<ProductSideEffect>,
    navActions: ProductNavActions,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    val lazyListState = rememberLazyListState()
    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior(
        canScroll = { lazyListState.canScrollForward },
        scrollBeforeContent = { false },
    )

    CollapsingTopBarLayout(
        topBar = {
            val mode = if (productState is ProductState.Success) {
                topBarModeAsState(lazyListState).value
            } else {
                TopBarMode.Transparent
            }

            TopBar(
                state = topBarState,
                onEvent = onTopBarEvent,
                mode = mode,
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .bottomNavBarPadding()
            .clipToBounds(),
    ) { padding ->

    }
}
