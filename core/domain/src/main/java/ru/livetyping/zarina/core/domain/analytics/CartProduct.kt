package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.analytics.model.CartProduct as AppMetricaCartProduct
import ru.livetyping.zarina.core.analytics.model.Product as AppMetricaProduct

public fun CartProduct.toAppMetricaProduct(): AppMetricaProduct {
    return AppMetricaProduct(
        id = productId.value,
        name = name,
        currentPrice = price.currentPrice,
        originalPrice = price.originalPrice,
    )
}

public fun CartProduct.toAppMetricaCartProduct(): AppMetricaCartProduct {
    return AppMetricaCartProduct(this.toAppMetricaProduct(), count)
}
