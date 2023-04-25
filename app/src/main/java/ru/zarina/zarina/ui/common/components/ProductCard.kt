package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO)
                .fillMaxWidth()
        ) {
            val image = product.media.firstOrNull { it.type == Media.Type.IMAGE }
            AsyncImageLoader(
                model = image?.url?.value,
                modifier = Modifier.fillMaxSize()
            )

            DiscountBadge(
                price = product.price,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.name,
            color = UiKitTheme.colors.primaryContentColor,
            style = UiKitTheme.typography.productCardName,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
fun ProductCardPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        ProductCard(
            product = product,
        )
    }
}
