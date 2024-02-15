package ru.zarina.zarina.ui.screen.products

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.ProductCardActions
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.Products
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.Tags
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.products.ProductsScreenComponents.TopBarActions
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.TagListState
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ProductsScreen(
    navigateForward: (ProductsScreenAction) -> Unit,
    navigateBackward: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val category by viewModel.category.collectAsStateWithLifecycle()
    val appliedFilterCount by viewModel.appliedFilterCount.collectAsStateWithLifecycle()
    val tagListState by viewModel.tagListState.collectAsStateWithLifecycle()
    val selectedTagId by viewModel.selectedTagId.collectAsStateWithLifecycle()

    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onSearchClicked = viewModel::onSearchClicked,
            onFiltersClicked = viewModel::onFiltersClicked,
        )
    }

    val productCardActions = remember(viewModel) {
        ProductCardActions(
            onProductClicked = viewModel::onProductClicked,
            onAddToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
            onAddToCartClicked = viewModel::onAddProductToCartClicked,
            onSubscribeClicked = viewModel::onSubscribeToProductClicked,
        )
    }

    BackHandler(onBack = viewModel::onSystemBackClicked)

    ScreenContent(
        category = category,
        appliedFilterCount = appliedFilterCount,
        topBarActions = topBarActions,
        tagListState = tagListState,
        selectedTagId = selectedTagId,
        onTagClicked = viewModel::onTagClicked,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        productCardActions = productCardActions,
        onRefreshProducts = viewModel::onRefreshProducts,
        onProductsErrorRefreshClicked = viewModel::onProductsErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    category: Category?,
    appliedFilterCount: Int,
    topBarActions: TopBarActions,
    tagListState: TagListState?,
    selectedTagId: Category.Id?,
    onTagClicked: (Category) -> Unit,
    productPagingDataFlow: Flow<PagingData<Product>>,
    productCardActions: ProductCardActions,
    onRefreshProducts: () -> Unit,
    onProductsErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<ProductsViewModel.SideEffect>,
    navigateForward: (ProductsScreenAction) -> Unit,
    navigateBackward: () -> Unit,
) {
    ProductsScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            title = category?.name,
            appliedFilterCount = appliedFilterCount,
            actions = topBarActions,
            modifier = Modifier.fillMaxWidth(),
        )

        Tags(
            state = tagListState,
            selectedTagId = selectedTagId,
            onTagClicked = onTagClicked,
        )

        Products(
            productPagingDataFlow = productPagingDataFlow,
            productCardActions = productCardActions,
            onRefreshProducts = onRefreshProducts,
            onProductsErrorRefreshClicked = onProductsErrorRefreshClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
