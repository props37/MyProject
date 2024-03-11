package ru.zarina.zarina.ui.screens.catalog.filters.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Shop
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ShopItem(
    shop: Shop?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = if (shop == null) 16.dp else 8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (shop != null) {
                Text(
                    text = stringResource(id = R.string.pickup_at_shop),
                    style = UiKitTheme.typography.circle1316,
                    color = UiKitTheme.colors.hint,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            val primaryText =
                shop?.name ?: stringResource(id = R.string.select_store)
            Text(
                text = primaryText,
                style = UiKitTheme.typography.circle1718,
                color = UiKitTheme.colors.primaryContentColor,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right_24),
            contentDescription = null,
            tint = UiKitTheme.colors.primaryContentColor,
        )
    }
}
