package ru.zarina.zarina.ui.screens.pickup.root.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ShopList(
    stocks: ImmutableList<Stock>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(stocks) { index, stock ->
            ShopItem(stock = stock)
        }
    }
}

@Composable
private fun ShopItem(
    stock: Stock,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = stock.shop.name,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stock.shop.address,
            style = UiKitTheme.typography.circle1518,
            color = UiKitTheme.colors.listItemSubtitle,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.working_schedule_template, stock.shop.address),
            style = UiKitTheme.typography.circle1518,
            color = UiKitTheme.colors.listItemSubtitle,
            textAlign = TextAlign.Start,
        )
    }
}
