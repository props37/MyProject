package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ProductRowCard(
    product: Product,
    onClick: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inactiveOverlayColor = UiKitTheme.colors.inactiveOverlay

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .drawWithContent {
                drawContent()
                if (!product.isAvailable) drawRect(inactiveOverlayColor)
            }
    ) {
        Box {
            AsyncImageLoader(
                url = product.media.first { it.type == Media.Type.IMAGE }.url.value,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .aspectRatio(
                        Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO,
                        matchHeightConstraintsFirst = true
                    ),
            )
            if (!product.isAvailable)
                OutOfStockBadge(
                    modifier = Modifier.align(Alignment.BottomStart)
                )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            if (product.attributes.isNotEmpty())
                Tag(
                    text = product.attributes.firstOrNull().orEmpty(),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            Text(
                text = product.name,
                color = UiKitTheme.colors.primaryContentColor,
                style = UiKitTheme.typography.circle1518,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                ProductPrice(
                    price = product.price,
                    textStyle = UiKitTheme.typography.circle1614,
                    modifier = Modifier.padding(end = 4.dp)
                )
                DiscountBadge(
                    price = product.price,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        var isFavorite by remember(product.isFavorite) { mutableStateOf(product.isFavorite) }
        FavoriteHeart(
            isFavorite = isFavorite,
            isShaking = false, // TODO
            onFavoriteChange = {
                onFavoriteChange(it)
                isFavorite = it
            },
            modifier = Modifier.align(Alignment.Top)
        )
    }
}
