package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.analytics.model.CartProduct as AppMetricaCartProduct
import ru.livetyping.zarina.core.analytics.model.Product as AppMetricaProduct

public fun Product.toAppMetricaProduct(): AppMetricaProduct {
    return AppMetricaProduct(
        id = id.value,
        name = name,
        currentPrice = price.currentPrice,
        originalPrice = price.originalPrice,
    )
}

public fun Product.toAppMetricaCartProduct(count: Int): AppMetricaCartProduct {
    return AppMetricaCartProduct(
        product = this.toAppMetricaProduct(),
        count = count,
    )
}
