package ru.livetyping.zarina.ui.screens.product.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.domain.old.Price
import ru.livetyping.zarina.ui.common.components.ProductPrice

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
