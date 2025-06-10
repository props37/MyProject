package ru.livetyping.zarina.core.domain.model.product

import java.math.BigDecimal
import java.math.RoundingMode

// Marked as stable on config/compose/stability_config.txt
public data class PodeliPrice(
    val payment: BigDecimal,
    val paymentCount: Int,
) {
    public companion object {
        public fun create(currentPrice: BigDecimal): PodeliPrice {
            val payment = currentPrice
                .divide(BigDecimal(PAYMENT_COUNT))
                .setScale(DECIMAL_PALCE_COUNT, RoundingMode.HALF_EVEN)
            return PodeliPrice(payment = payment, paymentCount = PAYMENT_COUNT)
        }

        private const val PAYMENT_COUNT = 4
        private const val DECIMAL_PALCE_COUNT = 2
    }
}