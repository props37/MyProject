package ru.livetyping.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.currentPrice
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ProductPrice(
    price: Price,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val priceTextStyle = UiKitTheme.typography.caption1.regular
        val originalPriceColor = if (price.hasDiscount) {
            UiKitTheme.colors.text.general.regular.disabled
        } else {
            UiKitTheme.colors.text.general.regular.default
        }
        val originalPriceTextDecoration = if (price.hasDiscount) {
            TextDecoration.LineThrough
        } else {
            TextDecoration.None
        }
        val discountColor = UiKitTheme.colors.text.general.accent.red

        val originalPrice = stringResource(
            id = R.string.price_in_rubles_string,
            rememberFormattedPrice(price.originalPrice),
        )
        Text(
            text = originalPrice.uppercase(),
            style = priceTextStyle,
            color = originalPriceColor,
            textDecoration = originalPriceTextDecoration,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (price.hasDiscount) {
            Spacer(modifier = Modifier.width(8.dp))

            val currentPrice = stringResource(
                id = R.string.price_in_rubles_string,
                rememberFormattedPrice(price.currentPrice),
            )
            Text(
                text = currentPrice.uppercase(),
                style = priceTextStyle,
                color = discountColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.discount_percent, price.discountPercent).uppercase(),
                style = UiKitTheme.typography.caption2.bold,
                color = discountColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWithoutDiscount() {
    ZarinaPreview {
        ProductPrice(
            price = remember {
                FakeDataGenerator.getPrice(
                    originalPrice = 4999,
                    hasDiscount = false,
                )
            },
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@Composable
private fun PreviewWithDiscount() {
    ZarinaPreview {
        ProductPrice(
            price = remember {
                FakeDataGenerator.getPrice(
                    originalPrice = 4999,
                    hasDiscount = true,
                )
            },
            modifier = Modifier.background(Color.White),
        )
    }
}
