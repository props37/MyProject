package ru.livetyping.zarina.core.domain.model.checkout

// Marked as stable on config/compose/stability_config.txt
public data class PaymentMethod(
    val id: Id,
    val type: PaymentMethodType,
    val title: String,
    val description: String,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)
}
