package ru.zarina.zarina.ui.screens.catalog.products

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.FilterBar
import ru.zarina.zarina.ui.common.components.ModalError
import ru.zarina.zarina.ui.common.components.ProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPadding
import ru.zarina.zarina.ui.common.components.color.ColorPickerDefaults
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.utils.domain.getStringResource
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreenContent(
    category: Category?,
    products: LazyPagingItems<Product>,
    productCount: Text?,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    sort: ProductSort,
    onSortClick: () -> Unit,
    isFilterButtonEnabled: Boolean,
    onFiltersClick: () -> Unit,
    onBackClick: () -> Unit,
    shakingFavorites: PersistentSet<Product.Id>,
    productGridState: LazyGridState = rememberLazyGridState(),
) {
    ZarinaScaffold(
        toolbar = {
            val context = LocalContext.current
            ScreenToolbar(
                title = category?.name.orEmpty(),
                subtitle = productCount?.getString(context),
                startIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        },
    ) {
        val colorPickerDimensions = ColorPickerDefaults.tinyDimensions()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val elevation = animateDpAsState(
                targetValue = if (productGridState.canScrollBackward) 6.dp else 0.dp,
                label = "filter bar elevation"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation.value)
                    .zIndex(1000f)
            ) {
                FilterBar(
                    sort = sort,
                    sortName = { stringResource(it.getStringResource()) },
                    onSortClick = onSortClick,
                    isFilterButtonEnabled = isFilterButtonEnabled,
                    onFiltersClick = onFiltersClick,
                    modifier = Modifier
                        .background(color = UiKitTheme.colors.screenBackground)
                        .fillMaxWidth(),
                )
            }
            val isLoading =
                products.loadState.append == LoadState.Loading || products.loadState.refresh == LoadState.Loading
            val errorState = when {
                !isLoading && products.itemCount == 0 -> ErrorState(
                    icon = R.drawable.ic_magnifying_glass_96,
                    title = Text.Resource(R.string.products_not_found),
                    subtitle = Text.Resource(R.string.try_changing_filter),
                    isButtonVisible = false,
                    buttonText = null
                )

                else -> null
            }
            if (errorState == null)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = productGridState,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                    modifier = Modifier
                        .fillMaxSize()
                        .bottomNavigationPadding()
                ) {
                    items(
                        count = products.itemCount,
                        key = products.itemKey { it.id.value },
                        contentType = products.itemContentType { null }
                    ) { productIndex ->
                        val product = products[productIndex]
                        if (product != null)
                            ProductCard(
                                product = product,
                                isMediaScrollable = true,
                                onClick = { onProductClick(product) },
                                isFavoriteShaking = shakingFavorites.contains(product.id),
                                onFavoriteChange = { onFavoriteChange(product, it) },
                                colorPickerDimensions = colorPickerDimensions,
                            )
                    }
                }
            else
                ModalError(
                    state = errorState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colors.screenBackground)
                        .navigationBarsPadding()
                        .bottomNavigationPadding()
                )
        }
    }
}

@Composable
fun ProductsScreen(
    savedStateHandle: SavedStateHandle,
    showProduct: (Product.Id) -> Unit,
    showSelectSort: () -> Unit,
    showFilters: () -> Unit,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<ProductsViewModel> { parametersOf(savedStateHandle) }

    val category by viewModel.category.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val isFilterButtonEnabled by viewModel.isFilterButtonEnabled.collectAsStateWithLifecycle()
    val shakingFavorites by viewModel.shakingFavorites.collectAsStateWithLifecycle()

    val productGridState = rememberLazyGridState()

    DisposableEffect(Unit) {
        viewModel.onIsForegroundChange(true)
        onDispose { viewModel.onIsForegroundChange(false) }
    }

    ProductsScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
        showSelectSort = showSelectSort,
        showFilters = showFilters,
        goBack = goBack,
        productGridState = productGridState,
    )

    ProductsScreenContent(
        category = category,
        products = products,
        productCount = productCount,
        onProductClick = viewModel::onProductClick,
        onFavoriteChange = viewModel::onFavoriteChange,
        sort = sort,
        onSortClick = viewModel::onSortClick,
        isFilterButtonEnabled = isFilterButtonEnabled,
        onFiltersClick = viewModel::onFiltersClick,
        onBackClick = viewModel::onBackClick,
        shakingFavorites = shakingFavorites,
        productGridState = productGridState,
    )
}

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<ProductsViewModel.SideEffect>,
    showProduct: (Product.Id) -> Unit,
    showSelectSort: () -> Unit,
    showFilters: () -> Unit,
    goBack: () -> Unit,
    productGridState: LazyGridState,
) {
    NavigationBarState(isVisible = true, isAnimated = true)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                ProductsViewModel.SideEffect.GoBack -> goBack()
                is ProductsViewModel.SideEffect.ShowProduct -> showProduct(effect.id)
                ProductsViewModel.SideEffect.ShowSelectSort -> showSelectSort()
                ProductsViewModel.SideEffect.ShowFilters -> showFilters()
                ProductsViewModel.SideEffect.ScrollProductsToTop -> productGridState.scrollToItem(0)
            }
        }
    }
}

@Preview
@Composable
fun ProductsScreenContentPreview() {
    ZarinaTheme {
        val products = flowOf<PagingData<Product>>(PagingData.empty()).collectAsLazyPagingItems()
        ProductsScreenContent(
            category = null,
            products = products,
            productCount = Text.String("12 товаров"),
            onProductClick = {},
            onFavoriteChange = { _, _ -> },
            sort = ProductSort.PRICE,
            onSortClick = {},
            isFilterButtonEnabled = false,
            onFiltersClick = {},
            onBackClick = {},
            shakingFavorites = persistentSetOf(),
        )
    }
}
