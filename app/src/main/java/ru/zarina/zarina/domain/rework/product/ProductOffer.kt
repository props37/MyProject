package ru.zarina.zarina.domain.rework.product

data class ProductOffer(
    val id: Id,
    val size: String,
    val sizeRu: String?,
    val isAvailable: Boolean,
    val height: String?,
    val barcode: String,
    val onlineCount: Int,
    val retailCount: Int,
) {
    @JvmInline
    value class Id(val value: String)
}
