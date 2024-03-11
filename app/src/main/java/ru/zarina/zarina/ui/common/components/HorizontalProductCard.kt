package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import ru.zarina.zarina.domain.old.Media
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.Size
import ru.zarina.zarina.ui.common.components.buttons.DropdownButton
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun HorizontalProductCard(
    product: Product,
    selectedSize: Size?,
    modifier: Modifier = Modifier,
    onSelectSizeClick: () -> Unit,
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
            selectedSize = selectedSize,
            onSelectSizeClick = onSelectSizeClick,
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
    selectedSize: Size?,
    onSelectSizeClick: () -> Unit,
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
        if (product.offers.size == 1)
            SingleProductSize(
                selectedSize = selectedSize,
            )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        ProductPrice(
            product = product
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        PickSizeButton(
            selectedSize = selectedSize,
            onClick = onSelectSizeClick,
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
        style = UiKitTheme.typography.circle1518,
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
    val colorName = product.color?.name ?: stringResource(R.string.unknown).lowercase()

    Text(
        text = stringResource(R.string.key_value, stringResource(R.string.color), colorName),
        color = UiKitTheme.colors.primaryContentColor,
        style = UiKitTheme.typography.circle1316,
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
            textStyle = UiKitTheme.typography.circle1614,
        )
        Spacer(modifier = Modifier.width(8.dp))
        DiscountBadge(
            price = product.price,
        )
    }
}

@Composable
private fun SingleProductSize(
    selectedSize: Size?,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(
            R.string.key_value,
            stringResource(R.string.size),
            selectedSize?.name.orEmpty()
        ),
        color = UiKitTheme.colors.primaryContentColor,
        style = UiKitTheme.typography.circle1316,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = modifier,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PickSizeButton(
    selectedSize: Size?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownButton(
        onClick = onClick,
        modifier = modifier
    ) {
        val text = selectedSize?.name ?: stringResource(R.string.select_size)
        AnimatedContent(
            targetState = text,
            label = "pick size button text"
        ) { state ->
            Text(
                text = state,
                color = UiKitTheme.colors.primaryContentColor,
                style = UiKitTheme.typography.circle1718,
                maxLines = 1,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "multiple sizes",
)
@Composable
fun HorizontalProductCardPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        HorizontalProductCard(
            product = product,
            selectedSize = product.offers.first().size,
            onSelectSizeClick = {},
        )
    }
}

@Preview(
    showBackground = true,
    name = "single size",
)
@Composable
fun HorizontalProductSingleSizeCardPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    val singleSizeProduct = product.copy(offers = product.offers.subList(0, 1))
    ZarinaTheme {
        HorizontalProductCard(
            product = singleSizeProduct,
            selectedSize = singleSizeProduct.offers.first().size,
            onSelectSizeClick = {},
        )
    }
}
