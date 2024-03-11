package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductHorizontalSection(
    title: String?,
    products: ImmutableList<Product>,
    shakingFavorites: ImmutableSet<Product.Id>,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(UiKitTheme.colorsOld.screenBackground)
    ) {
        if (title != null)
            Text(
                text = title,
                style = UiKitTheme.typography.circle2026bold,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
            )
        if (subtitle != null)
            Text(
                text = subtitle,
                style = UiKitTheme.typography.circle1518,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .padding(horizontal = 16.dp),
            )
        HorizontalPager(
            state = rememberPagerState { products.size },
            beyondBoundsPageCount = 1,
            key = { products[it].id.value },
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) { index ->
            val product = products[index]
            ProductCard(
                product = product,
                onClick = { onProductClick(product) },
                isFavoriteShaking = product.id in shakingFavorites,
                onFavoriteChange = { onFavoriteChange(product, it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}
