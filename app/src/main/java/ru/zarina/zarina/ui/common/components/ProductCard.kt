package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val inactiveOverlayColor = UiKitTheme.colors.inactiveOverlay
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .drawWithContent {
                drawContent()
                if (!product.isAvailable) drawRect(inactiveOverlayColor)
            }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO)
                .fillMaxWidth()
        ) {
            val image = product.media.firstOrNull { it.type == Media.Type.IMAGE }
            AsyncImageLoader(
                url = image?.url?.value,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                DiscountBadge(
                    price = product.price,
                )
                if (!product.isAvailable)
                    OutOfStockBadge()
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Tag(
            text = product.attributes.firstOrNull().orEmpty(),
            modifier = Modifier
        )
        Text(
            text = product.name,
            color = UiKitTheme.colors.primaryContentColor,
            style = UiKitTheme.typography.productCardName,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        val colors = remember(product) { product.colorVariants.map { it.key }.toImmutableList() }
        val selectedColor = remember(product) {
            product.colorVariants.entries.firstOrNull { it.value.isCurrent }?.key
        }
        SmallColorPicker(
            colors = colors,
            selectedColor = selectedColor,
        )
        Spacer(modifier = Modifier.height(4.dp))
        ProductPrice(
            price = product.price,
            textStyle = UiKitTheme.typography.productCardPrice,
        )
    }
}

@Composable
private fun Tag(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        color = UiKitTheme.colors.primaryContentColor,
        style = UiKitTheme.typography.productCardTag,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
)
@Composable
fun ProductCardPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        ProductCard(
            product = product,
            onClick = {},
        )
    }
}
