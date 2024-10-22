package ru.livetyping.zarina.core.domain.model.product

public data class ProductOffer(
    val id: Id,
    val size: String,
    val sizeRu: String?,
    val isAvailable: Boolean,
    val height: String?,
    val barcode: Barcode,
    val onlineCount: Int,
    val retailCount: Int,
) {
    @JvmInline
    public value class Id(public val value: String)
}
