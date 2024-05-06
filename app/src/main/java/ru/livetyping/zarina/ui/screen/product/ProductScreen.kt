package ru.livetyping.zarina.ui.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.product.ProductScreenComponents.ProductDetails
import ru.livetyping.zarina.ui.screen.product.ProductScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.product.ProductScreenComponents.TopBarMode
import ru.livetyping.zarina.ui.screen.product.ProductScreenComponents.topBarModeAsState
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductTotalLookState
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout

@Composable
fun ProductScreen(
    navigate: (ProductScreenAction) -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val productTotalLookState by viewModel.productTotalLookState.collectAsStateWithLifecycle()

    ScreenContent(
        productState = productState,
        onProductErrorRefreshClicked = viewModel::onProductErrorRefreshClicked,
        productTotalLookState = productTotalLookState,
        onProductTotalLookErrorRefreshClicked = viewModel::onProductTotalLookErrorRefreshClicked,
        onBackClicked = viewModel::onBackClicked,
        onShareClicked = viewModel::onShareClicked,
        onUrlClicked = viewModel::onUrlClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    productState: ProductState,
    onProductErrorRefreshClicked: () -> Unit,
    productTotalLookState: ProductTotalLookState,
    onProductTotalLookErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductScreenAction) -> Unit,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
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
                onBackClicked = onBackClicked,
                productName = (productState as? ProductState.Success)?.product?.name,
                onShareClicked = onShareClicked,
                mode = mode,
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding()
            .clipToBounds(),
    ) { padding ->
        val paddingModifier = if (productState !is ProductState.Success) {
            Modifier.padding(padding)
        } else {
            Modifier
        }

        ProductDetails(
            productState = productState,
            onProductErrorRefreshClicked = onProductErrorRefreshClicked,
            productTotalLookState = productTotalLookState,
            onProductTotalLookErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
            onUrlClicked = onUrlClicked,
            lazyListState = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .then(paddingModifier)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        )
    }
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
