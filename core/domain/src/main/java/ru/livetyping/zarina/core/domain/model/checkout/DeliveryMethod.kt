package ru.livetyping.zarina.core.domain.model.checkout

public data class DeliveryMethod(
    val id: Id,
    val type: DeliveryMethodType,
    val name: String,
    val description: String?,
) {
    @JvmInline
    public value class Id(public val value: Int)
}
