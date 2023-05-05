package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun HorizontalProductCard(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(16.dp)
    ) {
        Media(
            product = product,
            modifier = Modifier
                .aspectRatio(Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO)
                .weight(1f),
        )
        Spacer(
            modifier = Modifier.width(12.dp)
        )
        Information(
            product = product,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
private fun Media(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val image = product.media.firstOrNull { it.type == Media.Type.IMAGE }
    AsyncImageLoader(
        url = image?.url?.value,
        modifier = modifier
            .aspectRatio(Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO),
    )
}

@Composable
private fun Information(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        ProductName(
            product = product,
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        ProductColor(
            product = product
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        ProductPrice(
            product = product
        )
    }
}

@Composable
private fun ProductName(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Text(
        text = product.name,
        color = UiKitTheme.colors.primaryContentColor,
        style = UiKitTheme.typography.productCardName,
        maxLines = 2,
        textAlign = TextAlign.Start,
        modifier = modifier,
    )
}

@Composable
private fun ProductColor(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val color = product.colorVariants
        .entries
        .firstOrNull { (_, variant) -> variant.isCurrent }
        ?.key

    val colorName = color?.name ?: stringResource(R.string.unknown).lowercase()

    Text(
        text = stringResource(R.string.key_value, stringResource(R.string.color), colorName),
        color = UiKitTheme.colors.primaryContentColor,
        style = UiKitTheme.typography.productCardHorizontalColor,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = modifier,
    )
}

@Composable
private fun ProductPrice(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        ProductPrice(
            price = product.price,
            textStyle = UiKitTheme.typography.productCardHorizontalPrice,
        )
        Spacer(modifier = Modifier.width(8.dp))
        DiscountBadge(
            price = product.price,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun HorizontalProductCardPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        HorizontalProductCard(
            product = product,
        )
    }
}
