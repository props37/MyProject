package ru.livetyping.zarina.feature.product.ui.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.navigation.NavigationEntry

public abstract class ProductNavEntry : NavigationEntry {
    public abstract val productId: String

    public fun getProductId(): Product.Id = Product.Id(productId)
}
