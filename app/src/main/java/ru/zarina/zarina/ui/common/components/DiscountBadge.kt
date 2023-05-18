package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun DiscountBadge(
    price: Price,
    modifier: Modifier = Modifier,
) {
    if (price.isDiscounted)
        Box(
            modifier = modifier
                .background(UiKitTheme.colors.productBadgeBackground)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Text(
                text = stringResource(
                    R.string.discount_percentage,
                    (price.discount * 100f).toInt()
                ),
                style = UiKitTheme.typography.circle1012,
                color = UiKitTheme.colors.productBadgeForeground,
            )
        }
}
