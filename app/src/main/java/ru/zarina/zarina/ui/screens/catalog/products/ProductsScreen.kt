package ru.zarina.zarina.ui.screens.catalog.products

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductsScreenContent(
    products: LazyPagingItems<Product>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(
            count = products.itemCount,
            key = products.itemKey { it.id.value },
            contentType = products.itemContentType { null }
        ) { productIndex ->
            // TODO implement ui
            val product = products[productIndex]
            Text(product?.name.orEmpty())
        }
    }
}

@Composable
fun ProductsScreen() {
    val viewModel = hiltViewModel<ProductsViewModel>()

    val products = viewModel.products.collectAsLazyPagingItems()

    ProductsScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    ProductsScreenContent(
        products = products,
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
@FontScalePreviews
@DensityPreviews
@Composable
fun ProductsScreenContentPreview() {
    ZarinaTheme {
        val products = flowOf<PagingData<Product>>(PagingData.empty()).collectAsLazyPagingItems()
        ProductsScreenContent(
            products = products,
        )
    }
}
