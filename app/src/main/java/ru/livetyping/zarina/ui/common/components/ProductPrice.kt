package ru.livetyping.zarina.ui.common.components

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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.old.Price
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ProductPrice(
    price: Price,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = UiKitTheme.typographyOld.circle2028,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (price.isDiscounted) {
            Text(
                text = stringResource(R.string.currency_amount_rubles, price.original),
                style = textStyle,
                color = UiKitTheme.colorsOld.retiredPrice,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1
            )
        }
        val currentPriceColor =
            if (price.isDiscounted) UiKitTheme.colorsOld.price else UiKitTheme.colorsOld.primaryContentColor
        Text(
            text = stringResource(R.string.currency_amount_rubles, price.current),
            style = textStyle,
            color = currentPriceColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
