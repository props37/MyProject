package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.zarina.zarina.domain.old.Price
import ru.zarina.zarina.ui.common.components.ProductPrice

@Composable
fun PriceSection(
    price: Price,
    modifier: Modifier = Modifier,
) {
    ProductPrice(
        price = price,
        modifier = modifier
    )
}
