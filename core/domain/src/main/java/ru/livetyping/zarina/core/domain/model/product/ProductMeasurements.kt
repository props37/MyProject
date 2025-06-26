package ru.livetyping.zarina.core.domain.model.product

public data class ProductMeasurements(
    val entries: List<Entry>,
) {
    public data class Entry(
        val size: ProductSizeEn,
        val height: ProductHeight,
        val measurements: List<ProductMeasurement>,
    )
}

public data class ProductMeasurement(
    val title: String,
    val value: String,
)
