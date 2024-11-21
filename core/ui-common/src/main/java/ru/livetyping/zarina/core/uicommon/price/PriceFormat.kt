package ru.livetyping.zarina.core.uicommon.price

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import kotlin.text.Typography.nbsp

private const val PRICE_PATTERN = "#,###"

public fun formatPrice(price: Int): String {
    val format = getPriceDecimalFormat()
    return format.format(price)
}

public fun getPriceDecimalFormat(): DecimalFormat {
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = nbsp
    return DecimalFormat(PRICE_PATTERN, symbols)
}
