package ru.livetyping.zarina.feature.productsubscription.ui.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uimodel.product.ProductOfferParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductShortParcelable

public data class ProductSubscriptionNavParams(
    val product: Product,
    val offer: ProductOffer,
) {
    public fun toNavEntry(): ProductSubscriptionNavEntry {
        return ProductSubscriptionNavEntry(
            product = ProductShortParcelable.from(product),
            offer = ProductOfferParcelable.from(offer),
        )
    }
}
