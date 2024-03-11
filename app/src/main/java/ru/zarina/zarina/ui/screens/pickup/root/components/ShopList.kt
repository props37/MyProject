package ru.zarina.zarina.ui.screens.pickup.root.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Stock
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButton
import ru.zarina.zarina.ui.common.utils.domain.getStringResource
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ShopList(
    stocks: ImmutableList<Stock>,
    onStockClick: (Stock) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier,
    ) {
        itemsIndexed(stocks) { index, stock ->
            ShopItem(
                stock = stock,
                onClick = { onStockClick(stock) },
                modifier = Modifier.padding(16.dp)
            )
            if (index != stocks.lastIndex)
                Divider(
                    thickness = 1.dp,
                    color = UiKitTheme.colorsOld.listDivider,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
        }
    }
}

@Composable
fun ShopItem(
    stock: Stock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonVisible: Boolean = true,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stock.shop.name,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stock.shop.address,
            style = UiKitTheme.typography.circle1518,
            color = UiKitTheme.colorsOld.listItemSubtitle,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.working_schedule_template, stock.shop.schedule),
            style = UiKitTheme.typography.circle1518,
            color = UiKitTheme.colorsOld.listItemSubtitle,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(stock.amount.getStringResource()),
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colorsOld.primaryAccentColor,
            textAlign = TextAlign.Start,
        )
        if (isButtonVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaButton(
                onClick = onClick,
                padding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                modifier = Modifier.wrapContentSize(align = Alignment.Center),
            ) {
                Text(
                    text = stringResource(R.string.pickup_at_shop),
                    style = UiKitTheme.typography.circle1720bold,
                    color = UiKitTheme.colorsOld.primaryButtonForeground,
                )
            }
        }
    }
}
