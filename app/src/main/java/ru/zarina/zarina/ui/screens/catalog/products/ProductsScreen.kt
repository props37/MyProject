package ru.zarina.zarina.ui.screens.catalog.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.components.ProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreenContent(
    category: Category?,
    products: LazyPagingItems<Product>,
    onProductClick: (Product) -> Unit,
    onBackClick: () -> Unit,
) {
    val productGridState = rememberLazyGridState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = category?.name.orEmpty(),
                isElevated = productGridState.canScrollBackward,
                startIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        },
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = productGridState,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
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
                        onClick = { onProductClick(product) },
                    )
            }
            item(
                span = { GridItemSpan(2) }
            ) {
                GridLoader(
                    isVisible = products.loadState.append is LoadState.Loading || products.loadState.refresh is LoadState.Loading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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
fun ProductsScreen() {
    val viewModel = hiltViewModel<ProductsViewModel>()

    val category by viewModel.category.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()

    ProductsScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    ProductsScreenContent(
        category = category,
        products = products,
        onProductClick = viewModel::onProductClick,
        onBackClick = viewModel::onBackClick,
    )
}

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<ProductsViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
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
            onProductClick = {},
            onBackClick = {},
        )
    }
}
