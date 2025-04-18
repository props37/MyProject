package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.analytics.model.Product
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.analytics.model.CartProduct as AppMetricaCartProduct

public fun CartProduct.toAppMetricaCartProduct(): AppMetricaCartProduct {
    val product = Product(
        id = productId.value,
        name = name,
        currentPrice = price.currentPrice,
        originalPrice = price.originalPrice,
    )
    return AppMetricaCartProduct(product, count)
}
