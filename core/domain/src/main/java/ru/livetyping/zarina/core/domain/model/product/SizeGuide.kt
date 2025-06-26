package ru.livetyping.zarina.core.domain.model.product

public data class SizeGuide(
    val sizeEn: ProductSizeEn,
    val sizeRu: ProductSizeRu,
    val sizeFull: ProductSizeFull,
    val bust: String,
    val waist: String,
    val hips: String,
    val height: String,
)
