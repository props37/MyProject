package ru.livetyping.zarina.feature.product.ui.api

import ru.livetyping.zarina.core.domain.model.product.Product

public data class ProductNavParams(
    val productId: Product.Id,
) {
    public fun toNavEntry(): ProductNavEntry {
        return ProductNavEntry(productId.value)
    }
}
