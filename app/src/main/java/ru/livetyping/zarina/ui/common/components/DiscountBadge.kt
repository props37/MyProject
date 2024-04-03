package ru.livetyping.zarina.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.old.Price
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun DiscountBadge(
    price: Price,
    modifier: Modifier = Modifier,
) {
    if (price.isDiscounted)
        Box(
            modifier = modifier
                .background(UiKitTheme.colorsOld.productBadgeBackground)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Text(
                text = stringResource(
                    R.string.discount_percentage,
                    (price.discount * 100f).toInt()
                ),
                style = UiKitTheme.typographyOld.circle1012,
                color = UiKitTheme.colorsOld.productBadgeForeground,
            )
        }
}
