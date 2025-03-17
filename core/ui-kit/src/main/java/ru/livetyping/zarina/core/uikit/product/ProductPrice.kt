package ru.livetyping.zarina.core.uikit.product

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ProductPrice(
    price: ProductPrice,
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
            id = RCommon.string.res_price_in_rubles,
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
                id = RCommon.string.res_price_in_rubles,
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
                text = stringResource(
                    id = RCommon.string.res_discount_percent,
                    price.discountPercent.toString(),
                ).uppercase(),
                style = UiKitTheme.typography.caption2.bold,
                color = discountColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
