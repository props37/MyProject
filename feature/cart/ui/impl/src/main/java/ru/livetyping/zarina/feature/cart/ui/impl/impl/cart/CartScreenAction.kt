package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface CartScreenAction {
    data object BackClicked : CartScreenAction

    data object GoToCatalogClicked : CartScreenAction

    data class ChangeCityClicked(val currentCity: City?) : CartScreenAction

    data class ProductClicked(val product: CartProduct) : CartScreenAction

    data class ProductCountClicked(
        val productId: Product.Id,
        val barcode: Barcode,
        val initialCount: Int,
        val availableCount: Int,
        val cartType: CartType,
    ) : CartScreenAction

    data class CheckoutClicked(val cartType: CartType) : CartScreenAction
}
