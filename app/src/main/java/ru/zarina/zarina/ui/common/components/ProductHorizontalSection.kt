package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductHorizontalSection(
    title: String,
    products: ImmutableList<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(UiKitTheme.colors.screenBackground)
    ) {
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
            pageCount = products.size,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}
