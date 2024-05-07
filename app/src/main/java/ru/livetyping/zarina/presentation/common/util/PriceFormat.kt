package ru.livetyping.zarina.presentation.common.util

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.text.Typography.nbsp

private const val PRICE_PATTERN = "#,###"

@Composable
fun rememberFormattedPrice(price: Int): String {
    val format = remember { getPriceDecimalFormat() }
    return remember(price, format) { format.format(price) }
}

fun formatPrice(price: Int): String {
    val format = getPriceDecimalFormat()
    return format.format(price)
}

private fun getPriceDecimalFormat(): DecimalFormat {
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = nbsp
    return DecimalFormat(PRICE_PATTERN, symbols)
}
