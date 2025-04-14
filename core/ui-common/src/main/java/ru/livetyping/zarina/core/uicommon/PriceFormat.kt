package ru.livetyping.zarina.core.uicommon

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.math.BigDecimal
import kotlin.text.Typography.nbsp

private const val PRICE_PATTERN = "#,###"

public fun formatPrice(price: Int): String {
    val format = getPriceDecimalFormat()
    return format.format(price)
}

public fun formatPrice(price: BigDecimal): String {
    val format = getPriceDecimalFormat()
    return format.format(price)
}

public fun getPriceDecimalFormat(): DecimalFormat {
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = nbsp
    return DecimalFormat(PRICE_PATTERN, symbols)
}
