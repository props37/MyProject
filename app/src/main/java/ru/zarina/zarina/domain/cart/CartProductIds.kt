package ru.zarina.zarina.domain.cart

import ru.zarina.zarina.domain.product.Product

data class CartProductIds(
    val cartProductIds: Set<Product.Id>,

    /**
     * Can be larger than [cartProductIds] since a product can be added to the cart several times.
     */
    val cartProductCount: Int,
)
