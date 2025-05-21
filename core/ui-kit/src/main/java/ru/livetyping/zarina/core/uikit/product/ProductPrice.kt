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
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
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
        val priceTextStyle = UiKitTheme2.typography.body2

        val currentPrice = stringResource(
            id = RCommon.string.res_price_in_rubles,
            rememberFormattedPrice(price.currentPrice),
        )

        Text(
            text = currentPrice.uppercase(),
            style = priceTextStyle,
            color = UiKitTheme2.colors.mainBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        val discount = price.discount
        if (discount != null) {
            Spacer(modifier = Modifier.width(8.dp))

            val originalPrice = stringResource(
                id = RCommon.string.res_price_in_rubles,
                rememberFormattedPrice(price.originalPrice),
            )

            Text(
                text = originalPrice.uppercase(),
                style = priceTextStyle,
                color = UiKitTheme2.colors.middleGray,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
