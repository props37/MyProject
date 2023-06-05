package ru.zarina.zarina.ui.screens.catalog.products

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.components.ProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
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
    sort: ProductSort,
    onSortClick: () -> Unit,
    onBackClick: () -> Unit,
) {
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
    val productGridState = rememberLazyGridState()
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
        errorState = errorState,
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
                    onSortClick = onSortClick,
                    modifier = Modifier
                        .background(color = UiKitTheme.colors.screenBackground)
                        .fillMaxWidth(),
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = productGridState,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier.fillMaxSize()
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
                            colorPickerDimensions = colorPickerDimensions,
                        )
                }
                item(
                    span = { GridItemSpan(2) }
                ) {
                    GridLoader(
                        isVisible = isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterBar(
    sort: ProductSort,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.border(
            width = 1.dp,
            color = UiKitTheme.colors.primaryBorderColor,
        ),
    ) {
        FilterButton(
            icon = R.drawable.ic_sort_24,
            text = stringResource(sort.getStringResource()),
            onClick = onSortClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FilterButton(
    @DrawableRes
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = UiKitTheme.typography.circle1718,
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun GridLoader(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateEnterExit(
                    enter = fadeIn(),
                    exit = fadeOut(),
                )
                .padding(8.dp)
        ) {
            CircularProgressIndicator(
                color = UiKitTheme.colors.primaryContentColor,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun ProductsScreen(
    showProduct: (Product.Id) -> Unit,
    showSelectSort: () -> Unit,
    goBack: () -> Unit,
) {
    val viewModel = hiltViewModel<ProductsViewModel>()

    val category by viewModel.category.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()

    ProductsScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
        showSelectSort = showSelectSort,
        goBack = goBack,
    )

    ProductsScreenContent(
        category = category,
        products = products,
        productCount = productCount,
        onProductClick = viewModel::onProductClick,
        sort = sort,
        onSortClick = viewModel::onSortClick,
        onBackClick = viewModel::onBackClick,
    )
}

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<ProductsViewModel.SideEffect>,
    showProduct: (Product.Id) -> Unit,
    showSelectSort: () -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                ProductsViewModel.SideEffect.GoBack -> goBack()
                is ProductsViewModel.SideEffect.ShowProduct -> showProduct(effect.id)
                ProductsViewModel.SideEffect.ShowSelectSort -> showSelectSort()
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
            sort = ProductSort.PRICE,
            onSortClick = {},
            onBackClick = {},
        )
    }
}
