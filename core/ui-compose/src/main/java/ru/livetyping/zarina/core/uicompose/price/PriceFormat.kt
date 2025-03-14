package ru.livetyping.zarina.core.uicompose.price

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.livetyping.zarina.core.uicommon.getPriceDecimalFormat
import java.math.BigDecimal

@Composable
public fun rememberFormattedPrice(price: BigDecimal): String {
    val format = remember { getPriceDecimalFormat() }
    return remember(price, format) { format.format(price) }
}

@Composable
public fun rememberFormattedPrice(price: Int): String {
    val format = remember { getPriceDecimalFormat() }
    return remember(price, format) { format.format(price) }
}
