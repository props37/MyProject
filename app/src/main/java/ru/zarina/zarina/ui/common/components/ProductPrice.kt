package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Price
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun ProductPrice(
    price: Price,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = UiKitTheme.typography.circle2028,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (price.isDiscounted) {
            Text(
                text = stringResource(R.string.currency_amount_rubles, price.original),
                style = textStyle,
                color = UiKitTheme.colors.retiredPrice,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1
            )
        }
        val currentPriceColor =
            if (price.isDiscounted) UiKitTheme.colors.price else UiKitTheme.colors.primaryContentColor
        Text(
            text = stringResource(R.string.currency_amount_rubles, price.current),
            style = textStyle,
            color = currentPriceColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
