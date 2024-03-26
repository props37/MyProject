package ru.zarina.zarina.ui.common.utils.domain

import androidx.annotation.StringRes
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Stock

@StringRes
fun Stock.Amount.getStringResource() = when (this) {
    Stock.Amount.ONE -> R.string.last_chance
    Stock.Amount.FEW -> R.string.few
    Stock.Amount.SOME -> R.string.enough
    Stock.Amount.MANY -> R.string.many
}
