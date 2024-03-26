package ru.zarina.zarina.ui.common.utils.domain

import androidx.annotation.StringRes
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.ProductSort

@StringRes
fun ProductSort.getStringResource() = when (this) {
    ProductSort.DATE_DESCENDING -> R.string.by_new
    ProductSort.POPULARITY -> R.string.by_popularity
    ProductSort.PRICE -> R.string.by_price_ascending
    ProductSort.PRICE_DESCENDING -> R.string.by_price_descending
    ProductSort.DISCOUNT -> R.string.by_discount
}
