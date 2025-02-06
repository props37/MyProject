package ru.livetyping.zarina.presentation.screen.products

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import ru.livetyping.zarina.data.analytics.AppMetricaScreen
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.ProductGrid
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.products.ProductsScreenComponents.ProductsNotFoundPlaceholder
import ru.livetyping.zarina.presentation.screen.products.ProductsScreenComponents.Tags
import ru.livetyping.zarina.presentation.screen.products.ProductsScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.products.ProductsScreenComponents.TopBarActions
import ru.livetyping.zarina.presentation.screen.products.ProductsViewModel.TagListState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout

@Composable
fun ProductsScreen(
    navigate: (ProductsScreenAction) -> Unit,
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

    BackHandler(onBack = viewModel::onSystemBackClicked)

    ScreenContent(
        category = category,
        appliedFilterCount = appliedFilterCount,
        topBarActions = topBarActions,
        tagListState = tagListState,
        selectedTagId = selectedTagId,
        onTagClicked = viewModel::onTagClicked,
        productPagingDataFlow = viewModel.productPagingDataFlow,
        onProductClicked = viewModel::onProductClicked,
        onAddProductToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
        onAddProductToCartClicked = viewModel::onAddProductToCartClicked,
        onSubscribeToProductClicked = viewModel::onSubscribeToProductClicked,
        onRefreshProducts = viewModel::onRefreshProducts,
        onProductsErrorRefreshClicked = viewModel::onProductsErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
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
    productPagingDataFlow: Flow<PagingData<ProductItem>>,
    onProductClicked: (Product) -> Unit,
    onAddProductToFavoritesClicked: (Product) -> Unit,
    onAddProductToCartClicked: (Product) -> Unit,
    onSubscribeToProductClicked: (Product) -> Unit,
    onRefreshProducts: () -> Unit,
    onProductsErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<ProductsViewModel.SideEffect>,
    navigate: (ProductsScreenAction) -> Unit,
) {
    ProductsScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
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

        val tagsScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()
        CollapsingTopBarLayout(
            topBar = {
                Tags(
                    state = tagListState,
                    selectedTagId = selectedTagId,
                    onTagClicked = onTagClicked,
                )
            },
            scrollBehavior = tagsScrollBehavior,
            modifier = Modifier.clipToBounds(),
        ) { padding ->
            ProductGrid(
                productPagingDataFlow = productPagingDataFlow,
                onProductClicked = onProductClicked,
                onAddToFavoritesClicked = onAddProductToFavoritesClicked,
                onAddToCartClicked = onAddProductToCartClicked,
                onSubscribeClicked = onSubscribeToProductClicked,
                onProductsRefreshed = onRefreshProducts,
                onProductsErrorRefreshClicked = onProductsErrorRefreshClicked,
                noProductsPlaceholder = {
                    ProductsNotFoundPlaceholder(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    )
                },
                appMetricaScreen = AppMetricaScreen.ProductList,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .nestedScroll(tagsScrollBehavior.nestedScrollConnection),
            )
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            category = remember { FakeDataGenerator.getCategory() },
            appliedFilterCount = 4,
            topBarActions = remember { TopBarActions({}, {}, {}) },
            tagListState = remember {
                TagListState.TagList(FakeDataGenerator.getCategories().toImmutableList())
            },
            selectedTagId = null,
            onTagClicked = {},
            productPagingDataFlow = remember {
                flowOf(PagingData.from(FakeDataGenerator.getProductItems()))
            },
            onProductClicked = {},
            onAddProductToFavoritesClicked = {},
            onAddProductToCartClicked = {},
            onSubscribeToProductClicked = {},
            onRefreshProducts = {},
            onProductsErrorRefreshClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
