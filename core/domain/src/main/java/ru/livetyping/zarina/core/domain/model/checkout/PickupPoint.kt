package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Location

public sealed class PickupPoint {
    public abstract val id: Id
    public abstract val title: String
    public abstract val address: String
    public abstract val location: Location
    public abstract val isFittingAvailable: Boolean
    public abstract val isPaymentByCardAvailable: Boolean
    public abstract val availablePaymentMethods: Set<PaymentMethod>

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    public enum class PaymentMethod { CASH, CARD }
}
