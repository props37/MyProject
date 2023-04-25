package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.components.ProductCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompleteLookSection(
    products: List<Product>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        pageCount = products.size,
        beyondBoundsPageCount = 1,
        key = { products[it].id },
        contentPadding = PaddingValues(horizontal = 32.dp),
        modifier = modifier
    ) { index ->
        ProductCard(
            product = products[index],
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        )
    }
}
