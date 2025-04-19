package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.analytics.model.CartProduct as AppMetricaCartProduct
import ru.livetyping.zarina.core.analytics.model.Order as AppMetricaOrder
import ru.livetyping.zarina.core.analytics.model.Product as AppMetricaProduct

public fun OrderDetailed.toAppMetricaOrder(): AppMetricaOrder {
    return AppMetricaOrder(
        id = id.value,
        products = products.map { it.toAppMetricaCartProduct() },
    )
}

public fun OrderDetailed.Product.toAppMetricaCartProduct(): AppMetricaCartProduct {
    val product = AppMetricaProduct(
        id = productId.value,
        name = name,
        currentPrice = price.currentPrice,
        originalPrice = price.originalPrice,
    )
    return AppMetricaCartProduct(product, count)
}
