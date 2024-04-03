package ru.livetyping.zarina.ui.common.utils.domain

import androidx.annotation.StringRes
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.old.Stock

@StringRes
fun Stock.Amount.getStringResource() = when (this) {
    Stock.Amount.ONE -> R.string.last_chance
    Stock.Amount.FEW -> R.string.few
    Stock.Amount.SOME -> R.string.enough
    Stock.Amount.MANY -> R.string.many
}
