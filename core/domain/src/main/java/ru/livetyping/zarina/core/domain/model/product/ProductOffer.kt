package ru.livetyping.zarina.core.domain.model.product

import kotlin.text.Typography.nbsp

// Marked as stable on config/compose/stability_config.txt
public data class ProductOffer(
    val id: Id,
    val sizeEn: ProductSizeEn,
    val sizeRu: ProductSizeRu?,
    val isAvailable: Boolean,
    val height: ProductHeight?,
    val barcode: Barcode,
    val onlineCount: Int,
    val retailCount: Int,
) {
    val size: ProductSizeFull = run {
        val value = buildString {
            append(sizeEn.size)
            if (sizeRu != null) {
                append(nbsp)
                append("(${sizeRu.size})")
            }
        }
        ProductSizeFull(value)
    }

    val isAvailableInStores: Boolean get() = retailCount > 0

    @JvmInline
    public value class Id(public val value: String)
}
