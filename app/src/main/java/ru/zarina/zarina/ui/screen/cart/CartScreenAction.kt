package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.domain.product.Product

sealed class CartScreenAction {
    data object GoToCatalogClicked : CartScreenAction()

    data class CityClicked(val city: City?) : CartScreenAction()

    data class ProductCountClicked(
        val productId: Product.Id,
        val barcode: Barcode,
        val initialCount: Int,
        val availableCount: Int,
        val deliveryType: DeliveryType,
    ) : CartScreenAction()
}
