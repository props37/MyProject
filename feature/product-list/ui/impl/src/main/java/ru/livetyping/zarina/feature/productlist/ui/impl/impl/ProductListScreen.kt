package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.component.EmptyProductsPlaceholder
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.component.TagList
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarState

@Composable
internal fun ProductListScreen(
    navActions: ProductListNavActions,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val tagListState by viewModel.tagListState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        tagListState = tagListState,
        onTagListEvent = viewModel::onTagListEvent,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onProductEvent = viewModel::onProductEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    tagListState: TagListState,
    onTagListEvent: (TagListEvent) -> Unit,
    productPagingDataFlow: Flow<PagingData<ProductShort>>,
    onProductEvent: (ProductEvent) -> Unit,
    sideEffects: Flow<ProductListSideEffect>,
    navActions: ProductListNavActions,
) {
    ProductListScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(
            state = topBarState,
            onEvent = onTopBarEvent,
        )

        val tagListScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()
        CollapsingTopBarLayout(
            topBar = {
                TagList(
                    state = tagListState,
                    onEvent = onTagListEvent,
                )
            },
            scrollBehavior = tagListScrollBehavior,
            modifier = Modifier.clipToBounds(),
        ) { padding ->
            ProductGrid(
                productPagingDataFlow = productPagingDataFlow,
                onProductClicked = { onProductEvent(ProductEvent.ProductClicked(it)) },
                onAddToWishlistClicked = { onProductEvent(ProductEvent.AddToWishlistClicked(it)) },
                onAddToCartClicked = { onProductEvent(ProductEvent.AddToCartClicked(it)) },
                onSubscribeClicked = { onProductEvent(ProductEvent.SubscribeClicked(it)) },
                onProductsRefreshed = { onProductEvent(ProductEvent.ProductsRefreshed) },
                onProductsErrorRefreshClicked = {
                    onProductEvent(ProductEvent.ProductsErrorRefreshClicked)
                },
                emptyProductsPlaceholder = {
                    EmptyProductsPlaceholder(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .nestedScroll(tagListScrollBehavior.nestedScrollConnection),
            )
        }
    }
}
