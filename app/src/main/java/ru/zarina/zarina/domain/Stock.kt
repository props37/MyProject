package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Stock(
    val shop: Shop,
    val amount: Amount,
) : Parcelable {

    enum class Amount { ONE, FEW, SOME, MANY }

}
