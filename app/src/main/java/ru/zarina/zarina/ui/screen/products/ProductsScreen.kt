package ru.zarina.zarina.ui.screen.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.component.ProductCard
import ru.zarina.zarina.ui.common.component.ProductCardPlaceholder
import ru.zarina.zarina.ui.common.component.base.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.TopBarActions
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade
import java.io.IOException

@Composable
fun ProductsScreen(
    navigateBackward: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val category by viewModel.category.collectAsStateWithLifecycle()

    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onSearchClicked = viewModel::onSearchClicked,
            onFiltersClicked = viewModel::onFiltersClicked,
        )
    }

    ScreenContent(
        category = category,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        topBarActions = topBarActions,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    category: Category?,
    productPagingDataFlow: Flow<PagingData<Product>>,
    topBarActions: TopBarActions,
    sideEffects: Flow<ProductsViewModel.SideEffect>,
    navigateBackward: () -> Unit,
) {
    ProductsScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Top),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            title = category?.name,
            actions = topBarActions,
            modifier = Modifier.fillMaxWidth(),
        )

        // TODO: [High] Refactor

        val productPagingItems = productPagingDataFlow.collectAsLazyPagingItems()

        Crossfade(
            targetState = productPagingItems.loadState.refresh,
            contentKey = { it !is LoadState.Error },
            label = "", // TODO: [High] Add label
            modifier = Modifier.fillMaxSize(),
        ) { loadState ->
            if (loadState !is LoadState.Error) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // TODO: [High] Extract
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (loadState is LoadState.NotLoading) {
                        items(
                            count = productPagingItems.itemCount,
                            span = { index ->
                                if ((index + 1) % 5 == 0) {
                                    GridItemSpan(2)
                                } else {
                                    GridItemSpan(1)
                                }
                            },
                            key = productPagingItems.itemKey { it.id.value },
                        ) { index ->
                            val product = productPagingItems[index]
                            if (product != null) {
                                ProductCard(
                                    product = product,
                                    onClick = { /*TODO*/ },
                                    onAddToFavoritesClicked = { /*TODO*/ },
                                    onAddToCartClicked = { /*TODO*/ },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            } else {
                                ProductCardPlaceholder(modifier = Modifier.fillMaxWidth())
                            }
                        }
                    } else {
                        items(
                            count = 20,
                            span = { index ->
                                if ((index + 1) % 5 == 0) {
                                    GridItemSpan(2)
                                } else {
                                    GridItemSpan(1)
                                }
                            },
                        ) {
                            ProductCardPlaceholder(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            } else {
                val state = remember(loadState.error) {
                    when (loadState.error) {
                        is IOException -> ErrorStateRework.NETWORK
                        else -> ErrorStateRework.GENERIC
                    }
                }

                ZarinaErrorScreen(
                    state = state,
                    onRefreshClicked = { productPagingItems.retry() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
