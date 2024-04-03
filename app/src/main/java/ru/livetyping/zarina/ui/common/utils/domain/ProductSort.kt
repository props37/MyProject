package ru.livetyping.zarina.ui.common.utils.domain

import androidx.annotation.StringRes
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.old.ProductSort

@StringRes
fun ProductSort.getStringResource() = when (this) {
    ProductSort.DATE_DESCENDING -> R.string.by_new
    ProductSort.POPULARITY -> R.string.by_popularity
    ProductSort.PRICE -> R.string.by_price_ascending
    ProductSort.PRICE_DESCENDING -> R.string.by_price_descending
    ProductSort.DISCOUNT -> R.string.by_discount
}
