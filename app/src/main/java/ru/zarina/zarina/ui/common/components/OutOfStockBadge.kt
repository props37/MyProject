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
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun OutOfStockBadge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(UiKitTheme.colors.productBadgeBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = stringResource(R.string.out_of_stock),
            style = UiKitTheme.typography.productBadge,
            color = UiKitTheme.colors.productBadgeForeground,
        )
    }
}
