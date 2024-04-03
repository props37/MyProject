package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.common.Barcode

data class ProductOffer(
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
    value class Id(val value: String)
}
