package ru.livetyping.zarina.presentation.screen.cart

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.product.Product

sealed class CartScreenAction {
    data object GoToCatalogClicked : CartScreenAction()

    data class CityClicked(val currentCity: City?) : CartScreenAction()

    data class ProductCountClicked(
        val productId: Product.Id,
        val barcode: Barcode,
        val initialCount: Int,
        val availableCount: Int,
        val cartType: CartType,
    ) : CartScreenAction()

    data class CheckoutClicked(val cartType: CartType) : CartScreenAction()
}
