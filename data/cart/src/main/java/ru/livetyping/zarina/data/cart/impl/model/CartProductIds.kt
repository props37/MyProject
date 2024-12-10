package ru.livetyping.zarina.data.cart.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal data class CartProductIds(
    val cartProductIds: Set<Product.Id>,

    /**
     * Can be larger than [cartProductIds] since a product can be added to the cart several times.
     */
    val cartProductCount: Int,
)
