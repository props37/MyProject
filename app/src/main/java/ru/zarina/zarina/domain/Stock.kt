package ru.zarina.zarina.domain

data class Stock(
    val shop: Shop,
    val amount: Amount,
) {

    enum class Amount { ONE, FEW, SOME, MANY }

}
